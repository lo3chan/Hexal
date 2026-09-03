package ram.talia.hexal.api.casting.iota;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import kotlin.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import ram.talia.hexal.api.gates.GateManager;
import ram.talia.hexal.common.lib.hex.HexalIotaTypes;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class GateIota extends Iota {

    public record Payload(int index, @Nullable Either<Vec3, EntityAnchor> target) {
        public static final Codec<Payload> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("index").forGetter(Payload::index),
                Codec.either(Vec3.CODEC, EntityAnchor.CODEC).optionalFieldOf("target", null).forGetter(Payload::target)
        ).apply(instance, Payload::new));
    }

    public record EntityAnchor(UUID uuid, String name, Vec3 offset) {
        public static final Codec<EntityAnchor> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                UUIDUtil.CODEC.fieldOf("uuid").forGetter(EntityAnchor::uuid),
                Codec.STRING.fieldOf("name").forGetter(EntityAnchor::name),
                Vec3.CODEC.fieldOf("offset").forGetter(EntityAnchor::offset)
        ).apply(instance, EntityAnchor::new));
    }

    private final Payload payload;

    public GateIota(int index, @Nullable Either<Vec3, Pair<Entity, Vec3>> target) {
        super(() -> HexalIotaTypes.GATE);
        this.payload = new Payload(index, target == null ? null : target.mapRight(pair -> new EntityAnchor(pair.getFirst().getUUID(), pair.getFirst().getName().getString(), pair.getSecond())));
    }

    public GateIota(Payload payload) {
        super(() -> HexalIotaTypes.GATE);
        this.payload = payload;
    }

    public int getGateIndex() {
        return this.payload.index();
    }

    public @Nullable Either<Vec3, EntityAnchor> getTarget() {
        return this.payload.target();
    }

    public @Nullable Vec3 getTargetPos(ServerLevel level) {
        var target = this.getTarget();
        if (target == null) return null;

        return target.map(vec3 -> vec3, entityAnchor -> {
            var entity = level.getEntity(entityAnchor.uuid());
            if (entity == null) return null;
            return entity.position().add(entityAnchor.offset());
        });
    }

    public boolean isDrifting() {
        return this.getTarget() == null;
    }

    public boolean isLocationAnchored() {
        return this.getTarget() != null && this.getTarget().left().isPresent();
    }

    public boolean isEntityAnchored() {
        return this.getTarget() != null && this.getTarget().right().isPresent();
    }

    public Set<UUID> getMarked() {
        return GateManager.allMarked.getOrDefault(this.getGateIndex(), new HashSet<>());
    }

    public Set<Entity> getMarked(ServerLevel level) {
        Set<Entity> out = new HashSet<>();
        for (UUID uuid : this.getMarked()) {
            Entity entity = level.getEntity(uuid);
            if (entity != null) out.add(entity);
        }
        return out;
    }

    public boolean isMarked(Entity entity) {
        return GateManager.isMarked(this.getGateIndex(), entity);
    }

    public void mark(Entity entity) {
        GateManager.mark(this.getGateIndex(), entity.getUUID());
    }

    public void unmark(Entity entity) {
        GateManager.unmark(this.getGateIndex(), entity.getUUID());
    }

    public void clearMarked() {
        GateManager.clearMarked(this.getGateIndex());
    }

    public int getNumMarked() {
        return GateManager.allMarked.getOrDefault(this.getGateIndex(), new HashSet<>()).size();
    }

    @Override
    public boolean isTruthy() {
        return true;
    }

    @Override
    public boolean toleratesOther(Iota that) {
        return typesMatch(this, that) && that instanceof GateIota g && g.getGateIndex() == this.getGateIndex();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getType(), this.getGateIndex());
    }

    @Override
    public Component display() {
        if (this.isDrifting()) {
            return Component.translatable("hexal.spelldata.gate", this.getGateIndex()).withStyle(ChatFormatting.LIGHT_PURPLE);
        }
        var target = this.getTarget();
        if (target == null) {
            return Component.translatable("hexal.spelldata.gate", this.getGateIndex()).withStyle(ChatFormatting.LIGHT_PURPLE);
        }
        return target.map(
                vec3 -> Component.translatable("hexal.spelldata.gate", this.getGateIndex()).append(String.format(" (%.2f, %.2f, %.2f)", vec3.x, vec3.y, vec3.z)).withStyle(ChatFormatting.LIGHT_PURPLE),
                entityAnchor -> {
                    var offsetStr = String.format("%.2f, %.2f, %.2f", entityAnchor.offset().x, entityAnchor.offset().y, entityAnchor.offset().z);
                    var anchorStr = String.format(" (%s, %s)", entityAnchor.name(), offsetStr);
                    return Component.translatable("hexal.spelldata.gate", this.getGateIndex()).append(anchorStr).withStyle(ChatFormatting.LIGHT_PURPLE);
                });
    }

    public static IotaType<GateIota> TYPE = new IotaType<>() {
        private final MapCodec<GateIota> CODEC = Payload.CODEC.xmap(GateIota::new, g -> g.payload).fieldOf("gate");

        @Override
        public MapCodec<GateIota> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, GateIota> streamCodec() {
            return ByteBufCodecs.fromCodecWithRegistries(Payload.CODEC).map(GateIota::new, g -> g.payload);
        }

        @Override
        public int color() {
            return 0xff_ff55ff;
        }
    };
}
