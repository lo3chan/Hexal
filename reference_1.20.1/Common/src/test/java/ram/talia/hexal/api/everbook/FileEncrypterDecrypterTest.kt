package ram.talia.hexal.api.everbook

import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import net.minecraft.core.Registry
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.Bootstrap
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.util.*

internal class FileEncrypterDecrypterTest {

	lateinit var fileEncrypterDecrypter: FileEncrypterDecrypter

	@BeforeEach
	fun setUp() {
		val secretKey = FileEncrypterDecrypter.getKey(UUID.randomUUID(), "AES")
		fileEncrypterDecrypter = FileEncrypterDecrypter(secretKey, "AES/CBC/PKCS5Padding")
	}

	@AfterEach
	fun tearDown() {
		File("baz.enc").delete() // cleanup
	}

	@Test
	fun testEncryptDecrypt() {
		val originalContent = "foobar"

		fileEncrypterDecrypter.encrypt(originalContent, File("baz.enc"))
		val decryptedContent = fileEncrypterDecrypter.decrypt(File("baz.enc"))

		assertEquals(originalContent, decryptedContent)
	}

	@Test
	fun testEncryptDecryptCompound() {
		val originalContent = CompoundTag()
		originalContent.putString("testKey", "testVal")

		fileEncrypterDecrypter.encrypt(originalContent, File("baz.enc"))
		val decryptedContent = fileEncrypterDecrypter.decryptCompound(File("baz.enc"))

		assertEquals(originalContent, decryptedContent)
	}

	@Test
	fun testEncryptDecryptEverbook() {
		val everbook = Everbook(UUID.fromString("41C82C87-7AfB-4024-BA57-13D2C99CAE77"))

		everbook.setIota(HexPattern.fromAngles("a", HexDir.EAST), DoubleIota(15.0))

		fileEncrypterDecrypter.encrypt(everbook.serialiseToNBT(), File("baz.enc"))
		val decryptedContent = fileEncrypterDecrypter.decryptCompound(File("baz.enc"))

		assertEquals(everbook.serialiseToNBT(), decryptedContent)
	}

	companion object {
		@BeforeAll
		@JvmStatic
		fun setUpOnce() {
			// hack: we don't need bootstrap to succeed, we just need to set isBootstrapped so we can create registries
			try {
				Bootstrap.bootStrap()
			} catch (_: Throwable) {}

			HexIotaTypes.registerTypes { t, id -> Registry.register(HexIotaTypes.REGISTRY, id, t) }
		}
	}
}