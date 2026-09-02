package ram.talia.hexal

import at.petrak.hexcasting.api.addldata.ADHexHolder
import at.petrak.hexcasting.api.addldata.ADIotaHolder
import at.petrak.hexcasting.api.addldata.ADMediaHolder
import at.petrak.hexcasting.api.addldata.ADVariantItem
import at.petrak.hexcasting.api.casting.ActionRegistryEntry
import at.petrak.hexcasting.api.casting.arithmetic.Arithmetic
import at.petrak.hexcasting.api.casting.castables.SpecialHandler
import at.petrak.hexcasting.api.casting.eval.ResolvedPattern
import at.petrak.hexcasting.api.casting.eval.sideeffects.EvalSound
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM
import at.petrak.hexcasting.api.casting.eval.vm.ContinuationFrame
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.api.pigment.ColorProvider
import at.petrak.hexcasting.api.pigment.FrozenPigment
import at.petrak.hexcasting.api.player.AltioraAbility
import at.petrak.hexcasting.api.player.FlightAbility
import at.petrak.hexcasting.api.player.Sentinel
import at.petrak.hexcasting.common.lib.HexRegistries
import at.petrak.hexcasting.common.msgs.IMessage
import at.petrak.hexcasting.interop.pehkui.PehkuiInterop
import at.petrak.hexcasting.xplat.IXplatAbstractions
import at.petrak.hexcasting.xplat.IXplatTags
import at.petrak.hexcasting.xplat.Platform
import com.mojang.serialization.Lifecycle
import net.minecraft.core.BlockPos
import net.minecraft.core.MappedRegistry
import net.minecraft.core.Registry
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Tier
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraft.world.phys.Vec3
import java.util.function.BiFunction

// Implement each method only when it's needed for a test to work.
@Suppress("unused")
internal class HexalTestHexplatImpl : IXplatAbstractions {
    override fun platform(): Platform {
        TODO("Not yet implemented")
    }

    override fun isModPresent(id: String?): Boolean {
        TODO("Not yet implemented")
    }

    override fun isPhysicalClient(): Boolean {
        TODO("Not yet implemented")
    }

    override fun initPlatformSpecific() {
        TODO("Not yet implemented")
    }

    override fun sendPacketToPlayer(target: ServerPlayer?, packet: IMessage?) {
        TODO("Not yet implemented")
    }

    override fun sendPacketNear(pos: Vec3?, radius: Double, dimension: ServerLevel?, packet: IMessage?) {
        TODO("Not yet implemented")
    }

    override fun sendPacketTracking(entity: Entity?, packet: IMessage?) {
        TODO("Not yet implemented")
    }

    override fun toVanillaClientboundPacket(message: IMessage?): Packet<ClientGamePacketListener> {
        TODO("Not yet implemented")
    }

    override fun setBrainsweepAddlData(mob: Mob?) {
        TODO("Not yet implemented")
    }

    override fun isBrainswept(mob: Mob?): Boolean {
        TODO("Not yet implemented")
    }

    override fun setPigment(target: Player?, colorizer: FrozenPigment?): FrozenPigment? {
        TODO("Not yet implemented")
    }

    override fun setSentinel(target: Player?, sentinel: Sentinel?) {
        TODO("Not yet implemented")
    }

    override fun setFlight(target: ServerPlayer?, flight: FlightAbility?) {
        TODO("Not yet implemented")
    }

    override fun setAltiora(target: Player?, altiora: AltioraAbility?) {
        TODO("Not yet implemented")
    }

    override fun setStaffcastImage(target: ServerPlayer?, image: CastingImage?) {
        TODO("Not yet implemented")
    }

    override fun setPatterns(target: ServerPlayer?, patterns: MutableList<ResolvedPattern>?) {
        TODO("Not yet implemented")
    }

    override fun getFlight(player: ServerPlayer?): FlightAbility? {
        TODO("Not yet implemented")
    }

    override fun getAltiora(player: Player?): AltioraAbility? {
        TODO("Not yet implemented")
    }

    override fun getPigment(player: Player?): FrozenPigment {
        TODO("Not yet implemented")
    }

    override fun getSentinel(player: Player?): Sentinel? {
        TODO("Not yet implemented")
    }

    override fun getStaffcastVM(player: ServerPlayer?, hand: InteractionHand?): CastingVM {
        TODO("Not yet implemented")
    }

    override fun getPatternsSavedInUi(player: ServerPlayer?): MutableList<ResolvedPattern> {
        TODO("Not yet implemented")
    }

    override fun clearCastingData(player: ServerPlayer?) {
        TODO("Not yet implemented")
    }

    override fun findMediaHolder(stack: ItemStack?): ADMediaHolder? {
        TODO("Not yet implemented")
    }

    override fun findMediaHolder(player: ServerPlayer?): ADMediaHolder? {
        TODO("Not yet implemented")
    }

    override fun findDataHolder(stack: ItemStack?): ADIotaHolder? {
        TODO("Not yet implemented")
    }

    override fun findDataHolder(entity: Entity?): ADIotaHolder? {
        TODO("Not yet implemented")
    }

    override fun findHexHolder(stack: ItemStack?): ADHexHolder? {
        TODO("Not yet implemented")
    }

    override fun findVariantHolder(stack: ItemStack?): ADVariantItem? {
        TODO("Not yet implemented")
    }

    override fun isPigment(stack: ItemStack?): Boolean {
        TODO("Not yet implemented")
    }

    override fun getColorProvider(pigment: FrozenPigment?): ColorProvider {
        TODO("Not yet implemented")
    }

    override fun addEquipSlotFabric(slot: EquipmentSlot?): Item.Properties {
        TODO("Not yet implemented")
    }

    override fun <T : BlockEntity?> createBlockEntityType(
        func: BiFunction<BlockPos, BlockState, T>?,
        vararg blocks: Block?
    ): BlockEntityType<T> {
        TODO("Not yet implemented")
    }

    override fun tryPlaceFluid(level: Level?, hand: InteractionHand?, pos: BlockPos?, fluid: Fluid?): Boolean {
        TODO("Not yet implemented")
    }

    override fun drainAllFluid(level: Level?, pos: BlockPos?): Boolean {
        TODO("Not yet implemented")
    }

    override fun isCorrectTierForDrops(tier: Tier?, bs: BlockState?): Boolean {
        TODO("Not yet implemented")
    }

    override fun getUnsealedIngredient(stack: ItemStack?): Ingredient {
        TODO("Not yet implemented")
    }

    override fun tags(): IXplatTags {
        TODO("Not yet implemented")
    }

    override fun isShearsCondition(): LootItemCondition.Builder {
        TODO("Not yet implemented")
    }

    override fun getModName(namespace: String?): String {
        TODO("Not yet implemented")
    }

    override fun getActionRegistry(): Registry<ActionRegistryEntry> {
        TODO("Not yet implemented")
    }

    override fun getSpecialHandlerRegistry(): Registry<SpecialHandler.Factory<*>> {
        TODO("Not yet implemented")
    }

    override fun getIotaTypeRegistry(): Registry<IotaType<*>> {
        return MappedRegistry(HexRegistries.IOTA_TYPE, Lifecycle.stable())
    }

    override fun getArithmeticRegistry(): Registry<Arithmetic> {
        TODO("Not yet implemented")
    }

    override fun getContinuationTypeRegistry(): Registry<ContinuationFrame.Type<*>> {
        TODO("Not yet implemented")
    }

    override fun getEvalSoundRegistry(): Registry<EvalSound> {
        TODO("Not yet implemented")
    }

    override fun isBreakingAllowed(world: ServerLevel?, pos: BlockPos?, state: BlockState?, player: Player?): Boolean {
        TODO("Not yet implemented")
    }

    override fun isPlacingAllowed(
        world: ServerLevel?,
        pos: BlockPos?,
        blockStack: ItemStack?,
        player: Player?
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun getPehkuiApi(): PehkuiInterop.ApiAbstraction {
        TODO("Not yet implemented")
    }
}