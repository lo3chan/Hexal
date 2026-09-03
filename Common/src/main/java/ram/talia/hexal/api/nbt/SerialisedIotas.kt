package ram.talia.hexal.api.nbt

import ram.talia.hexal.api.parseIota
import ram.talia.hexal.api.toNbt
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.utils.asCompound
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import java.util.*

class SerialisedIotaList(private var tag: ListTag?, private var iotas: MutableList<Iota>?) {

    constructor(iotas: MutableList<Iota>?) : this(null, iotas)
    constructor(tag: ListTag?) : this(tag, null)
    constructor() : this(null, null)

    private var level: ServerLevel? = null
    private var tagReferencedEntityUUIDs: MutableList<UUID>? = null
    private var tagReferencedEntitiesAreLoaded: MutableList<Boolean>? = null
    private var iotasReferencedEntities: MutableList<Entity>? = null

    private fun scanIotaForEntities(iota: Iota, entityList: MutableList<Entity>) {
        when (iota.type) {
            HexIotaTypes.ENTITY -> {
                val entity = level?.let { (iota as EntityIota).getEntity(it) }
                if (entity != null && !entityList.contains(entity)) {
                    entityList.add(entity)
                }
            }
            HexIotaTypes.LIST -> {
                for (subIota in (iota as ListIota).list) {
                    scanIotaForEntities(subIota, entityList)
                }
            }
        }
    }

    private fun scanTagForEntities(tag: CompoundTag, referencedEntityUUIDs: MutableList<UUID>) {
        val iota = parseIota(tag)
        if (iota is EntityIota) {
            val uuid = iota.entityId
            if (!referencedEntityUUIDs.contains(uuid)) {
                referencedEntityUUIDs.add(uuid)
            }
        } else if (iota is ListIota) {
            for (sub in iota.list) {
                if (sub is EntityIota) {
                    val uuid = sub.entityId
                    if (!referencedEntityUUIDs.contains(uuid)) {
                        referencedEntityUUIDs.add(uuid)
                    }
                }
            }
        }
    }

    fun clear() {
        iotas = null
        tag = null
        level = null
        tagReferencedEntityUUIDs = null
        tagReferencedEntitiesAreLoaded = null
        iotasReferencedEntities = null
    }

    fun set(iotas: MutableList<Iota>) {
        this.iotas = iotas
        tag = null
        level = null
        tagReferencedEntityUUIDs = null
        tagReferencedEntitiesAreLoaded = null
        iotasReferencedEntities = null
    }

    fun set(tag: ListTag) {
        this.tag = tag
        level = null
        iotas = null
        tagReferencedEntityUUIDs = null
        tagReferencedEntitiesAreLoaded = null
        iotasReferencedEntities = null
    }

    fun refreshIotas(level: ServerLevel) {
        if (tag != null) {
            var regenerateCache = (iotas == null) || (this.level != level)

            if (tagReferencedEntityUUIDs != null) {
                for (i in 0 until tagReferencedEntityUUIDs!!.size) {
                    val uuid = tagReferencedEntityUUIDs!![i]
                    val entityIsLoaded = tagReferencedEntitiesAreLoaded!![i]
                    val entity = level.getEntity(uuid)
                    if ((entity != null) != entityIsLoaded) {
                        regenerateCache = true
                        break
                    }
                }
            }

            if (regenerateCache) {
                tagReferencedEntityUUIDs = ArrayList()
                tagReferencedEntitiesAreLoaded = ArrayList()
                this.level = level
                iotas = tag?.toIotaList(level) ?: mutableListOf()

                if (tag != null) {
                    for (innerTag in tag!!) {
                        scanTagForEntities(innerTag.asCompound, tagReferencedEntityUUIDs!!)
                    }
                }

                for (uuid in tagReferencedEntityUUIDs!!) {
                    tagReferencedEntitiesAreLoaded!!.add(level.getEntity(uuid) != null)
                }
            }
        } else if (iotas != null) {
            if (iotasReferencedEntities == null) {
                iotasReferencedEntities = ArrayList()
                for (iota in iotas!!) {
                    scanIotaForEntities(iota, iotasReferencedEntities!!)
                }
            }

            var forceSerialize = false
            for (entity in iotasReferencedEntities!!) {
                if (entity.isRemoved) {
                    forceSerialize = true
                    break
                }
            }

            if (forceSerialize) {
                tag = iotas!!.toNbtList()
                iotas = tag!!.toIotaList(level)
                this.level = level
                iotasReferencedEntities = null
                tagReferencedEntityUUIDs = null
                tagReferencedEntitiesAreLoaded = null
            }
        }
    }

    fun getIotas(level: ServerLevel): List<Iota> {
        refreshIotas(level)
        return iotas ?: mutableListOf()
    }

    fun getTag(): ListTag {
        if ((tag == null) && (iotas != null)) {
            tag = iotas!!.toNbtList()
            iotas = null
            level = null
            iotasReferencedEntities = null
            tagReferencedEntityUUIDs = null
            tagReferencedEntitiesAreLoaded = null
        }
        return tag ?: ListTag()
    }

    fun add(iota: Iota) {
        if (tag != null) {
            val newTag = iota.toNbt()
            tag!!.add(newTag)
            this.level = null
            iotas = null
            tagReferencedEntityUUIDs = null
            tagReferencedEntitiesAreLoaded = null
            iotasReferencedEntities = null
        } else if (iotas != null) {
            iotas!!.add(iota)
            iotasReferencedEntities = null
        } else {
            iotas = mutableListOf(iota)
        }
    }

    fun add(tag: CompoundTag) {
        if (this.tag != null) {
            this.tag!!.add(tag)
            this.level = null
            iotas = null
            tagReferencedEntityUUIDs = null
            tagReferencedEntitiesAreLoaded = null
            iotasReferencedEntities = null
        } else if (iotas != null) {
            this.tag = iotas!!.toNbtList()
            this.tag!!.add(tag)
            this.level = null
            iotas = null
            iotasReferencedEntities = null
            tagReferencedEntityUUIDs = null
            tagReferencedEntitiesAreLoaded = null
        } else {
            this.tag = ListTag()
            this.tag!!.add(tag)
        }
    }

    fun pop(level: ServerLevel): Iota? {
        if (tag != null) {
            if (tag!!.size == 0) return null
            val poppedTag = tag!!.removeAt(0)
            this.level = null
            iotas = null
            tagReferencedEntityUUIDs = null
            tagReferencedEntitiesAreLoaded = null
            iotasReferencedEntities = null
            if (tag!!.size == 0) this.tag = null
            return parseIota(poppedTag.asCompound)
        } else if (iotas != null) {
            if (iotas!!.size == 0) return null
            val poppedIota = iotas!!.removeAt(0)
            iotasReferencedEntities = null
            return poppedIota
        } else {
            return null
        }
    }

    fun size(): Int {
        return if (tag != null) tag!!.size else if (iotas != null) iotas!!.size else 0
    }

    fun getReferencedEntities(level: ServerLevel): List<Entity> {
        if (tag != null) {
            if (tagReferencedEntityUUIDs == null) {
                tagReferencedEntityUUIDs = ArrayList()
                tagReferencedEntitiesAreLoaded = ArrayList()
                for (innerTag in tag!!) {
                    scanTagForEntities(innerTag.asCompound, tagReferencedEntityUUIDs!!)
                }
                for (uuid in tagReferencedEntityUUIDs!!) {
                    tagReferencedEntitiesAreLoaded!!.add(level.getEntity(uuid) != null)
                }
            }
            val referencedEntities: MutableList<Entity> = ArrayList()
            for (uuid in tagReferencedEntityUUIDs!!) {
                val entity = level.getEntity(uuid)
                if (entity != null) referencedEntities.add(entity)
            }
            return referencedEntities
        } else if (iotas != null) {
            if (iotasReferencedEntities == null) {
                iotasReferencedEntities = ArrayList()
                for (iota in iotas!!) {
                    scanIotaForEntities(iota, iotasReferencedEntities!!)
                }
            }
            return iotasReferencedEntities as List<Entity>
        } else {
            return ArrayList()
        }
    }
}

// Wrapper for SerializedIotaList (only ever used for ravenmind)
class SerialisedIota(private val iotaList: SerialisedIotaList = SerialisedIotaList(null, null)) {

    constructor(iota: Iota?) : this(SerialisedIotaList(if (iota == null) null else mutableListOf(iota)))
    constructor(tag: CompoundTag?) : this(SerialisedIotaList()) {
        if (tag != null) {
            iotaList.add(tag)
        }
    }
    constructor() : this(SerialisedIotaList())

    fun getTag(): CompoundTag {
        val listTag = iotaList.getTag()
        return if (listTag.size == 0) {
            NullIota().toNbt()
        } else {
            listTag[0].asCompound
        }
    }

    fun getIota(level: ServerLevel): Iota {
        val iotas = iotaList.getIotas(level)
        return if (iotas.isEmpty()) NullIota() else iotas[0]
    }

    fun set(iota: Iota) {
        iotaList.set(mutableListOf(iota))
    }

    fun set(tag: CompoundTag) {
        val listTag = ListTag()
        listTag.add(tag)
        iotaList.set(listTag)
    }

    fun getReferencedEntities(level: ServerLevel): List<Entity> {
        return iotaList.getReferencedEntities(level)
    }

    fun refreshIota(level: ServerLevel) {
        iotaList.refreshIotas(level)
    }
}
