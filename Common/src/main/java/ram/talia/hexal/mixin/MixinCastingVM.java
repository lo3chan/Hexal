package ram.talia.hexal.mixin;

import at.petrak.hexcasting.api.casting.eval.CastResult;
import at.petrak.hexcasting.api.casting.eval.ExecutionClientView;
import at.petrak.hexcasting.api.casting.eval.ResolvedPatternType;
import at.petrak.hexcasting.api.casting.eval.env.StaffCastEnv;
import at.petrak.hexcasting.api.casting.eval.sideeffects.EvalSound;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.PatternIota;
import at.petrak.hexcasting.api.casting.math.HexDir;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.common.lib.HexSounds;
import at.petrak.hexcasting.common.lib.hex.HexEvalSounds;
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ram.talia.hexal.api.HexalAPI;
import ram.talia.hexal.api.linkable.ILinkable;
import ram.talia.hexal.api.linkable.PlayerLinkstore;
import ram.talia.hexal.common.lib.HexalActions;
import ram.talia.hexal.xplat.IXplatAbstractions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mixin(CastingVM.class)
public abstract class MixinCastingVM {
	private static final String TAG_USINGMACRO = HexalAPI.MOD_ID + ":using_macro";

	@Shadow(remap = false) @Final private CastingImage image;

	@Shadow(remap = false)
	public abstract CastingImage getImage();

	private final CastingVM harness = (CastingVM) (Object) this;

	@Inject(method = "queueExecuteAndWrapIotas", at = @At("HEAD"), cancellable = true, remap = false)
	private void queueExecuteAndWrapIotas(List<? extends Iota> iotas, ServerLevel world, CallbackInfoReturnable<ExecutionClientView> cir) {
		if (iotas.isEmpty())
			return;

		var iota = iotas.get(0);
		var env = harness.getEnv();
		var image = harness.getImage();
		var escapeNext = image.getEscapeNext();

		List<Iota> toExecute;

		if (!(env instanceof StaffCastEnv))
			return;

		ServerPlayer caster = (ServerPlayer) env.getCastingEntity();
		if (caster == null)
			return;

		boolean isExecutingMacro = false;
		if (escapeNext || !env.isEnlightened())
			toExecute = new ArrayList<>(Collections.singleton(iota));
		else if (iota.getType() != HexIotaTypes.PATTERN
				|| ((PatternIota) iota).getPattern().sigsEqual(HexPattern.fromAngles("qqqaw", HexDir.EAST)))
			toExecute = new ArrayList<>(Collections.singleton(iota));
		else {
			HexPattern pattern = ((PatternIota) iota).getPattern();
			toExecute = IXplatAbstractions.INSTANCE.getEverbookMacro(caster, pattern);
			if (toExecute == null) {
				toExecute = new ArrayList<>(Collections.singleton(iota));
			} else {
				isExecutingMacro = true;
			}
		}
		image.getUserData().putBoolean(TAG_USINGMACRO, isExecutingMacro);

		boolean isUnescapedEscape = !escapeNext &&
				iota.getType() == HexIotaTypes.PATTERN &&
				((PatternIota) iota).getPattern().sigsEqual(HexPattern.fromAngles("qqqaw", HexDir.EAST));

		var transmittingTo = IXplatAbstractions.INSTANCE.getPlayerTransmittingTo(caster);
		boolean transmitting = transmittingTo != null;
		if (transmitting && !isUnescapedEscape) {
			var iter = toExecute.iterator();

			while (iter.hasNext()) {
				var it = iter.next();
				if (!escapeNext && iota.getType() == HexIotaTypes.PATTERN &&
						Iota.tolerates(iota, new PatternIota(HexalActions.LINK_COMM_CLOSE_TRANSMIT.prototype())))
					break;

				iter.remove();
				transmittingTo.receiveIota(IXplatAbstractions.INSTANCE.getLinkstore(caster), it);
			}

			harness.setImage(image.copy(image.getStack(), image.getParenCount(), image.getParenthesized(), false, image.getSimulateNext(), image.getOpsConsumed(), image.getUserData()));
		}

		boolean wasTransmitting = transmitting;
		var ret = harness.queueExecuteAndWrapIotas(toExecute, world);
		if (isExecutingMacro) {
			Vec3 soundPos = caster.position();
			SoundEvent sound = HexSounds.ADD_TO_PATTERN.value();
			float pitch = 1f;
			if (ret.getResolutionType() == ResolvedPatternType.EVALUATED) {
				sound = HexSounds.CAST_SPELL.value();
				pitch = 0.9f;
			} else if (ret.getResolutionType() == ResolvedPatternType.ESCAPED) {
				sound = HexSounds.CAST_NORMAL.value();
			}
			env.getWorld().playSound((Player) null, soundPos.x, soundPos.y, soundPos.z, sound, SoundSource.PLAYERS, 1f, pitch);
		}
		transmittingTo = IXplatAbstractions.INSTANCE.getPlayerTransmittingTo(caster);
		transmitting = transmittingTo != null;
		boolean isEdgeTransmit = transmitting ^ wasTransmitting;
		boolean isStackClear = ret.isStackClear() && !transmitting;
		ResolvedPatternType type = (transmitting && !isUnescapedEscape && !isEdgeTransmit) ? ResolvedPatternType.ESCAPED : ret.getResolutionType();

		ret = ret.copy(isStackClear, type, ret.getStackDescs(), ret.getRavenmind());
		cir.setReturnValue(ret);
	}

	@ModifyVariable(method = "queueExecuteAndWrapIotas", at = @At(value = "STORE", ordinal = 0), remap = false)
	private CastResult makeResultQuiet(CastResult result) {
		CastingImage existingImage = result.getNewData();
		if (harness.getEnv() instanceof StaffCastEnv && existingImage != null && !existingImage.getEscapeNext()) {
			CompoundTag data = existingImage.getUserData();
			if (data.contains(TAG_USINGMACRO) && data.getBoolean(TAG_USINGMACRO)) {
				result = result.copy(result.getCast(), result.getContinuation(), result.getNewData(), result.getSideEffects(), result.getResolutionType(), HexEvalSounds.MUTE.get());
			}
		}
		return result;
	}
}
