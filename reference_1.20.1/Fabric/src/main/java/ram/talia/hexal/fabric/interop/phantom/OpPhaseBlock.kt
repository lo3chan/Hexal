package ram.talia.hexal.fabric.interop.phantom

import at.petrak.hexcasting.api.casting.*
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3
import ram.talia.hexal.api.config.HexalConfig
import ram.talia.hexal.fabric.network.MsgPhaseBlockS2C
import ram.talia.hexal.xplat.IXplatAbstractions

object OpPhaseBlock : SpellAction {
    override val argc = 2

    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val pos = args.getBlockPos(0, argc)
        val time = args.getPositiveDouble(1, argc)

        env.assertPosInRangeForEditing(pos)

        val bs: BlockState = env.world.getBlockState(pos)
        if (bs.getDestroySpeed(env.world, pos) < 0.0f)
            throw MishapBadBlock.of(pos, "phaseable")

        return SpellAction.Result(
            Spell(pos, (time * 20).toInt()),
            (HexalConfig.server.phaseBlockCostFactor * time * time).toLong(),
            listOf(ParticleSpray.burst(Vec3.atCenterOf(pos), 0.5))
        )
    }

    private data class Spell(val pos: BlockPos, val ticks: Int) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            val blockstate = env.world.getBlockState(pos)
            if (IXplatAbstractions.INSTANCE.isBreakingAllowed(env.world, pos, blockstate, env.castingEntity as? ServerPlayer)) {
                env.world.phaseBlock(pos, ticks)
                IXplatAbstractions.INSTANCE.sendPacketTracking(pos, env.world, MsgPhaseBlockS2C(pos, ticks))
            }
        }
    }
}
