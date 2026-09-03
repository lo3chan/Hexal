package ram.talia.hexal.common.recipe

import at.petrak.hexcasting.common.lib.HexStateIngredients
import at.petrak.hexcasting.common.recipe.ingredient.state.StateIngredient
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.HolderLookup
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

data class FreezeRecipe(val blockIn: StateIngredient, val result: BlockState) : Recipe<SingleRecipeInput> {
	override fun matches(input: SingleRecipeInput, level: Level) = false

	fun matches(blockIn: BlockState) = this.blockIn.test(blockIn)

	override fun assemble(input: SingleRecipeInput, registries: HolderLookup.Provider): ItemStack = ItemStack.EMPTY

	override fun canCraftInDimensions(pWidth: Int, pHeight: Int) = false

	override fun getResultItem(registries: HolderLookup.Provider): ItemStack = ItemStack.EMPTY.copy()

	override fun getSerializer(): RecipeSerializer<*> = HexalRecipeSerializers.FREEZE

	override fun getType(): RecipeType<*> = HexalRecipeTypes.FREEZE_TYPE

	class Serializer : RecipeSerializer<FreezeRecipe> {
		private val CODEC: MapCodec<FreezeRecipe> = RecordCodecBuilder.mapCodec { instance ->
			instance.group(
				HexStateIngredients.TYPED_CODEC.fieldOf("blockIn").forGetter { it.blockIn },
				BlockState.CODEC.fieldOf("result").forGetter { it.result }
			).apply(instance, ::FreezeRecipe)
		}

		private val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, FreezeRecipe> = StreamCodec.composite(
			HexStateIngredients.TYPED_STREAM_CODEC,
			FreezeRecipe::blockIn,
			ByteBufCodecs.fromCodec(BlockState.CODEC),
			FreezeRecipe::result,
			::FreezeRecipe
		)

		override fun codec(): MapCodec<FreezeRecipe> = CODEC

		override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, FreezeRecipe> = STREAM_CODEC
	}
}
