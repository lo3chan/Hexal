package ram.talia.hexal.forge.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import ram.talia.hexal.api.HexalAPI;
import ram.talia.hexal.common.network.*;

public record HexalPacketPayload(ResourceLocation id, byte[] data) implements CustomPacketPayload {
    public static final Type<HexalPacketPayload> TYPE = new Type<>(HexalAPI.modLoc("channel"));

    public static final StreamCodec<ByteBuf, HexalPacketPayload> STREAM_CODEC = StreamCodec.of(
        (buf, payload) -> {
            FriendlyByteBuf fbuf = new FriendlyByteBuf(buf);
            fbuf.writeResourceLocation(payload.id());
            fbuf.writeByteArray(payload.data());
        },
        buf -> {
            FriendlyByteBuf fbuf = new FriendlyByteBuf(buf);
            ResourceLocation id = fbuf.readResourceLocation();
            byte[] data = fbuf.readByteArray();
            return new HexalPacketPayload(id, data);
        }
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleClient(HexalPacketPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            ByteBuf buffer = Unpooled.wrappedBuffer(payload.data());
            ResourceLocation id = payload.id();

            if (id.equals(MsgAddRenderLinkS2C.ID)) {
                MsgAddRenderLinkS2C.handle(MsgAddRenderLinkS2C.deserialise(buffer));
            } else if (id.equals(MsgRemoveRenderLinkS2C.ID)) {
                MsgRemoveRenderLinkS2C.handle(MsgRemoveRenderLinkS2C.deserialise(buffer));
            } else if (id.equals(MsgSetRenderLinksAck.ID)) {
                MsgSetRenderLinksAck.handle(MsgSetRenderLinksAck.deserialise(buffer));
            } else if (id.equals(MsgSingleParticleAck.ID)) {
                MsgSingleParticleAck.handle(MsgSingleParticleAck.deserialise(buffer));
            } else if (id.equals(MsgParticleLinesAck.ID)) {
                MsgParticleLinesAck.handle(MsgParticleLinesAck.deserialise(buffer));
            } else if (id.equals(MsgWispCastSoundS2C.ID)) {
                MsgWispCastSoundS2C.handle(MsgWispCastSoundS2C.deserialise(buffer));
            } else if (id.equals(MsgSetEverbookS2C.ID)) {
                MsgSetEverbookS2C.handle(MsgSetEverbookS2C.deserialise(buffer));
            } else if (id.equals(MsgRemoveEverbookS2C.ID)) {
                MsgRemoveEverbookS2C.handle(MsgRemoveEverbookS2C.deserialise(buffer));
            } else if (id.equals(MsgToggleMacroS2C.ID)) {
                MsgToggleMacroS2C.handle(MsgToggleMacroS2C.deserialise(buffer));
            }
        });
    }

    public static void handleServer(HexalPacketPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            ByteBuf buffer = Unpooled.wrappedBuffer(payload.data());
            ResourceLocation id = payload.id();

            if (id.equals(MsgSendEverbookC2S.ID)) {
                MsgSendEverbookC2S msg = MsgSendEverbookC2S.deserialise(buffer);
                if (context.player() instanceof ServerPlayer serverPlayer) {
                    msg.handle(serverPlayer.server, serverPlayer);
                }
            }
        });
    }
}