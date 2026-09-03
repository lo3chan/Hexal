package ram.talia.hexal.api

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.pigment.FrozenPigment
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps

fun FrozenPigment.toNbt(): CompoundTag =
	(FrozenPigment.CODEC.encodeStart(NbtOps.INSTANCE, this).result().orElseGet { CompoundTag() }) as? CompoundTag ?: CompoundTag()

fun parseFrozenPigment(tag: CompoundTag): FrozenPigment =
	FrozenPigment.CODEC.parse(NbtOps.INSTANCE, tag).result().orElseGet { FrozenPigment.DEFAULT.get() }

fun HexPattern.toNbt(): CompoundTag =
	(HexPattern.CODEC.encodeStart(NbtOps.INSTANCE, this).result().orElseGet { CompoundTag() }) as? CompoundTag ?: CompoundTag()

fun parseHexPattern(tag: CompoundTag): HexPattern =
	HexPattern.CODEC.parse(NbtOps.INSTANCE, tag).result().orElseGet { HexPattern.fromAngles("", HexDir.NORTH_EAST) }

fun Iota.toNbt(): CompoundTag =
	(IotaType.TYPED_CODEC.encodeStart(NbtOps.INSTANCE, this).result().orElseGet { CompoundTag() }) as? CompoundTag ?: CompoundTag()

fun parseIota(tag: CompoundTag): Iota =
	IotaType.TYPED_CODEC.parse(NbtOps.INSTANCE, tag).result().orElseGet { NullIota() }
