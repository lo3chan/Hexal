package ram.talia.hexal.common.network

import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.common.msgs.IMessage
import io.netty.buffer.ByteBuf
import net.minecraft.client.Minecraft
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import ram.talia.hexal.api.HexalAPI
import ram.talia.hexal.api.everbook.Everbook
import ram.talia.hexal.xplat.IClientXplatAbstractions

data class MsgSetEverbookS2C(val key: HexPattern, val iota: CompoundTag) : IMessage {
	override fun serialize(buf: FriendlyByteBuf) {
		buf.writeEnum(key.startDir)
		buf.writeUtf(key.anglesSignature())
		buf.writeNbt(iota)
	}

	override fun getFabricId() = ID

	companion object {
		@JvmField
		val ID: ResourceLocation = HexalAPI.modLoc("setever")

		@JvmStatic
		fun deserialise(buffer: ByteBuf): MsgSetEverbookS2C {
			val buf = FriendlyByteBuf(buffer)
			val dir = buf.readEnum(at.petrak.hexcasting.api.casting.math.HexDir::class.java)
			val angles = buf.readUtf()
			val pattern = HexPattern.fromAngles(angles, dir)
			return MsgSetEverbookS2C(pattern, buf.readNbt()!!)
		}

		@JvmStatic
		fun handle(self: MsgSetEverbookS2C) {
			Minecraft.getInstance().execute {
				IClientXplatAbstractions.INSTANCE.setClientEverbookIota(self.key, self.iota)
				Everbook.notifyModification()
			}
		}
	}
}