package ram.talia.hexal.api.casting.iota;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import ram.talia.hexal.api.mediafieditems.ItemRecord;
import ram.talia.hexal.api.mediafieditems.MediafiedItemManager;
import ram.talia.hexal.common.lib.hex.HexalIotaTypes;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class MoteIota extends Iota {
    public static final String TAG_TEMP_STORAGE = "hexal:temp_storage";

    private final MediafiedItemManager.Index itemIndex;

    public MoteIota(MediafiedItemManager.Index itemIndex) {
        super(() -> HexalIotaTypes.MOTE);
        this.itemIndex = itemIndex;
    }

    public static @Nullable MoteIota makeIfStorageLoaded(ItemStack stack, UUID storageUUID) {
        var index = MediafiedItemManager.assignItem(stack, storageUUID);
        return index != null ? new MoteIota(index) : null;
    }

    public static @Nullable MoteIota makeIfStorageLoaded(ItemRecord record, UUID storageUUID) {
        var index = MediafiedItemManager.assignItem(record, storageUUID);
        return index != null ? new MoteIota(index) : null;
    }

    public @Nullable MoteIota selfOrNull() {
        return this.isEmpty() ? null : this;
    }

    public MediafiedItemManager.Index getItemIndex() {
        return this.itemIndex;
    }

    public @Nullable ItemRecord getRecord() {
        var rec = MediafiedItemManager.getRecord(this.itemIndex);
        return rec != null ? rec.get() : null;
    }

    public @Nullable Item getItem() {
        var rec = this.getRecord();
        return rec != null ? rec.getItem() : null;
    }

    public long getCount() {
        var rec = this.getRecord();
        return rec != null ? rec.getCount() : 0L;
    }

    public boolean isEmpty() {
        return this.getRecord() == null || this.getCount() <= 0L;
    }

    public List<ItemStack> getStacksToDrop(int maxStackSize) {
        return MediafiedItemManager.getStacksToDrop(this.itemIndex, maxStackSize);
    }

    public List<ItemStack> getStacksToDrop(int count, int maxStackSize) {
        return MediafiedItemManager.getStacksToDrop(this.itemIndex, count, maxStackSize);
    }

    public long absorb(MoteIota other) {
        return MediafiedItemManager.mergeStores(this.itemIndex, other.itemIndex);
    }

    public long absorb(MoteIota other, long count) {
        return MediafiedItemManager.mergeStores(this.itemIndex, other.itemIndex, count);
    }

    public @Nullable MoteIota split(long count) {
        var index = MediafiedItemManager.splitStore(this.itemIndex, count);
        return index != null ? new MoteIota(index) : null;
    }

    public @Nullable MoteIota template() {
        var index = MediafiedItemManager.templateIndex(this.itemIndex);
        return index != null ? new MoteIota(index) : null;
    }

    public boolean isTemplate() {
        return this.itemIndex.getUuid().equals(MediafiedItemManager.TEMPLATE_STORAGE);
    }

    public boolean isSameType(MoteIota other) {
        var rec1 = this.getRecord();
        var rec2 = other.getRecord();
        return rec1 != null && rec2 != null && rec1.itemEquals(rec2);
    }

    public boolean isSameStorage(MoteIota other) {
        return this.itemIndex.getUuid().equals(other.itemIndex.getUuid());
    }

    public boolean isSameStorage(UUID storageUUID) {
        return this.itemIndex.getUuid().equals(storageUUID);
    }

    @Override
    public boolean isTruthy() {
        return !this.isEmpty();
    }

    @Override
    public boolean toleratesOther(Iota that) {
        return typesMatch(this, that) && that instanceof MoteIota m && m.itemIndex.equals(this.itemIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getType(), this.itemIndex);
    }

    @Override
    public Component display() {
        var rec = this.getRecord();
        if (rec == null) {
            return Component.translatable("hexcasting.tooltip.null_iota").withStyle(ChatFormatting.GRAY);
        }
        return Component.translatable("hexal.spelldata.mote", rec.getDisplayName().getString(), rec.getCount()).withStyle(ChatFormatting.YELLOW);
    }

    public static final Codec<MediafiedItemManager.Index> INDEX_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            UUIDUtil.CODEC.fieldOf("storage").forGetter(MediafiedItemManager.Index::getUuid),
            Codec.INT.fieldOf("index").forGetter(MediafiedItemManager.Index::getIndex)
    ).apply(instance, MediafiedItemManager.Index::new));

    public static IotaType<MoteIota> TYPE = new IotaType<>() {
        private final MapCodec<MoteIota> CODEC = INDEX_CODEC.xmap(MoteIota::new, m -> m.itemIndex).fieldOf("mote");

        @Override
        public MapCodec<MoteIota> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<ByteBuf, MoteIota> streamCodec() {
            return ByteBufCodecs.fromCodec(INDEX_CODEC).map(MoteIota::new, m -> m.itemIndex);
        }

        @Override
        public int color() {
            return 0xff_ffff55;
        }
    };
}
