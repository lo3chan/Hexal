package ram.talia.hexal.common.recipe

import at.petrak.hexcasting.common.recipe.RecipeSerializerBase
import at.petrak.hexcasting.common.recipe.ingredient.StateIngredient
import at.petrak.hexcasting.common.recipe.ingredient.StateIngredientHelper
import com.google.gson.JsonObject
import net.minecraft.core.RegistryAccess
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.GsonHelper
import net.minecraft.world.Container
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.HolderLookup
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput

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
				StateIngredient.CODEC.fieldOf("blockIn").forGetter { it.blockIn },
				BlockState.CODEC.fieldOf("result").forGetter { it.result }
			).apply(instance, ::FreezeRecipe)
		}

		private val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, FreezeRecipe> = StreamCodec.of(
			{ buf, recipe ->
				recipe.blockIn.write(buf)
				buf.writeVarInt(Block.getId(recipe.result))
			},
			{ buf ->
				val blockIn = StateIngredientHelper.read(buf)
				val result = Block.stateById(buf.readVarInt())
				FreezeRecipe(blockIn, result)
			}
		)

		override fun codec(): MapCodec<FreezeRecipe> = CODEC
		override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, FreezeRecipe> = STREAM_CODEC
	}
}
