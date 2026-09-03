package ram.talia.hexal.common.casting.arithmetics.operator.mote

import at.petrak.hexcasting.api.casting.arithmetic.operator.Operator.Companion.downcast
import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorUnary
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate.all
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate.ofType
import at.petrak.hexcasting.api.casting.iota.NullIota
import net.minecraft.world.item.ItemStack
import ram.talia.hexal.api.toIntCapped
import ram.talia.hexal.common.lib.hex.HexalIotaTypes.MOTE

fun apply(iota: Iota): Iota {
    val mote = downcast(iota, MOTE)
    val record = mote.record ?: return NullIota()
    val stack = record.toStack(mote.count.toIntCapped())

    return NullIota()
}

object OperatorMoteExtractItem : OperatorUnary(all(ofType(MOTE)), ::apply)
