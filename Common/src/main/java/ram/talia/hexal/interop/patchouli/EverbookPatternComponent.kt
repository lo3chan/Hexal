package ram.talia.hexal.interop.patchouli

import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.api.utils.*
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import at.petrak.hexcasting.interop.patchouli.AbstractPatternComponent
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style
import net.minecraft.network.chat.TextColor
import net.minecraft.network.chat.contents.TranslatableContents
import net.minecraft.util.FormattedCharSequence
import ram.talia.hexal.xplat.IClientXplatAbstractions
import vazkii.patchouli.api.IComponentRenderContext
import vazkii.patchouli.api.IVariable
import vazkii.patchouli.client.book.gui.GuiBook
import java.util.function.UnaryOperator

@Suppress("SameParameterValue", "unused")
class EverbookPatternComponent : AbstractPatternComponent() {
	@Transient
	var indexNum: Int = -1
	@Transient
	var isMacro = false
	@Transient
	var iota: CompoundTag? = null

	override fun build(x: Int, y: Int, pagenum: Int) {
		super.build(x, if (y != -1 && y != 70) { y } else { 50 }, pagenum)
		indexNum = pagenum - 1
	}

	override fun getPatterns(lookup: UnaryOperator<IVariable>): List<HexPattern> {
		val pattern = IClientXplatAbstractions.INSTANCE.getClientEverbookPattern(indexNum) ?: return listOf()

		isMacro = IClientXplatAbstractions.INSTANCE.isClientEverbookMacro(pattern)
		iota = IClientXplatAbstractions.INSTANCE.getClientEverbookIota(pattern)

		return listOf(pattern)
	}

	override fun onDisplayed(context: IComponentRenderContext) {
		val level = Minecraft.getInstance().level
		if (level != null) {
			onVariablesAvailable({ it }, level.registryAccess())
		}
	}

	override fun render(graphics: GuiGraphics, ctx: IComponentRenderContext, partialTicks: Float, mouseX: Int, mouseY: Int) {
		val poseStack = graphics.pose()
		poseStack.pushPose()
		poseStack.translate(HEADER_X.toDouble(), HEADER_Y.toDouble(), 0.0)

		val headerComponent = (if (isMacro) "hexal.everbook_pattern_entry.macro_header" else "hexal.everbook_pattern_entry.header")
				.asTranslatedComponent(indexNum)
				.setStyle(Style.EMPTY.withFont(Minecraft.UNIFORM_FONT))

		drawCenteredStringNoShadow(graphics, headerComponent.string, 0, 0, 0)
		poseStack.popPose()

		drawWrappedIota(graphics, iota, DATA_X, DATA_Y, 0)

		super.render(graphics, ctx, partialTicks, mouseX, mouseY)
	}

	override fun showStrokeOrder() = true

	private fun drawCenteredStringNoShadow(graphics: GuiGraphics, s: String, x: Int, y: Int, colour: Int) {
		val font = Minecraft.getInstance().font
		graphics.drawString(font, s, x - font.width(s) / 2, y, colour, false)
	}

	private fun drawWrappedIota(graphics: GuiGraphics, iota: CompoundTag?, x: Int, y: Int, colour: Int) {
		val ms = graphics.pose()

		if (iota == null)
			return

		val font = Minecraft.getInstance().font

		val iotaText = getDisplayWithMaxWidth(iota, GuiBook.PAGE_WIDTH, font).iterator()

		var currentY = y

		while (iotaText.hasNext() && currentY <= y + 5 * 9) { // don't draw more lines than fit in the book.
			ms.pushPose()
			ms.translate(x.toDouble(), currentY.toDouble(), 0.0)
			val toDraw = if (currentY < y + 5 * 9) { iotaText.next() } else { "...".red.visualOrderText }
			graphics.drawString(font, toDraw, 0, 0, colour, false)
			ms.popPose()
			currentY += 9
		}
	}

	private fun getDisplayWithMaxWidth(tag: CompoundTag, maxWidth: Int, font: Font): List<FormattedCharSequence> {
		val level = Minecraft.getInstance().level ?: return font.split(brokenIota(), maxWidth)
		val iota = IotaType.TYPED_CODEC.parse(level.registryAccess().createSerializationContext(NbtOps.INSTANCE), tag).result().orElse(null)
			?: return font.split(brokenIota(), maxWidth)
		val display = iota.display().copy().replaceStyle(::replaceWhite).withStyle { it.withFont(Minecraft.UNIFORM_FONT) }
		return font.split(display, maxWidth)
	}

	private fun brokenIota(): Component {
		return Component.translatable("hexcasting.spelldata.unknown")
			.withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC)
	}

	private fun replaceWhite(style: Style): Style = if (style.color == TextColor.fromLegacyFormat(ChatFormatting.WHITE))
		style.withColor(ChatFormatting.DARK_RED)
		else style

	private fun MutableComponent.replaceStyle(replacer: (Style) -> Style): MutableComponent {
		val contents = this.contents

		this.styledWith(replacer)

		if (contents !is TranslatableContents)
			return this

		contents.args.forEach {
			if (it is MutableComponent)
				it.replaceStyle(replacer)
		}

		return this
	}

	companion object {
		const val HEADER_X = 58
		const val HEADER_Y = 35

		const val DATA_X = 10
		const val DATA_Y = 90
	}
}