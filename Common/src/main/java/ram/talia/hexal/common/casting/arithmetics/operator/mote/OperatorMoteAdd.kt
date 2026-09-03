package ram.talia.hexal.common.casting.arithmetics.operator.mote

import at.petrak.hexcasting.api.casting.arithmetic.operator.Operator
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate.all
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate.ofType
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.OperationResult
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.utils.TreeList
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds
import ram.talia.hexal.api.casting.iota.MoteIota
import ram.talia.hexal.api.mediafieditems.MediafiedItemManager
import ram.talia.hexal.common.lib.hex.HexalIotaTypes.MOTE

object OperatorMoteAdd : Operator(2, all(ofType(MOTE))) {
    override fun operate(env: CastingEnvironment, image: CastingImage, continuation: SpellContinuation): OperationResult {
        val stack = image.stack
        val size = stack.size

        // Pop 2 iotas from the stack (last two)
        val absorberIota = if (size >= 2) stack[size - 2] else NullIota()
        val absorbeeIota = if (size >= 1) stack[size - 1] else NullIota()

        val absorber = (absorberIota as? MoteIota)?.selfOrNull()
        val absorbee = (absorbeeIota as? MoteIota)?.selfOrNull()

        // Remove the last 2 elements
        val base = if (size >= 2) stack.dropRight(2) else TreeList.empty<Iota>()

        val results: List<Iota> = if (absorber == null || absorbee == null) {
            val toReturn = listOfNotNull(absorber, absorbee)
            toReturn.ifEmpty { listOf(NullIota()) }
        } else if (absorber.itemIndex == absorbee.itemIndex) {
            listOf(absorber)
        } else if (!absorber.typeMatches(absorbee)) {
            throw MishapInvalidIota.of(absorbee, 0, "cant_combine_motes")
        } else {
            MediafiedItemManager.merge(absorber.itemIndex, absorbee.itemIndex)
            listOf(absorber)
        }

        val newStack = base.appendedAll(results)
        val newImage = image.copy(newStack, image.parenCount, image.parenthesized, image.escapeNext, image.simulateNext, image.opsConsumed, image.userData)
        return OperationResult(newImage, listOf(), continuation, HexEvalSounds.NORMAL_EXECUTE.get())
    }
}
