package ram.talia.hexal.common.casting.actions.spells.link

import ram.talia.hexal.api.caster
import at.petrak.hexcasting.api.casting.*
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv
import at.petrak.hexcasting.api.casting.iota.Iota
import ram.talia.hexal.api.casting.mishaps.MishapNoLinked
import ram.talia.hexal.api.casting.mishaps.MishapNonPlayer
import ram.talia.hexal.xplat.IXplatAbstractions

object OpOpenTransmit : ConstMediaAction {
	override val argc = 1

	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val player = env.caster ?: throw MishapNonPlayer()
		val playerLinkable = IXplatAbstractions.INSTANCE.getLinkstore(player)

		if (playerLinkable.numLinked() == 0)
			throw MishapNoLinked(playerLinkable)

		val index = args.getPositiveIntUnder(0, argc, playerLinkable.numLinked())

		IXplatAbstractions.INSTANCE.setPlayerTransmittingTo(player, index)

		return listOf()
	}
}