package com.numosophy.utility

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.util.Base64

object KeyStoreHelper {

    private const val KEYSTORE_PROVIDER = "AndroidKeyStore"

    fun generateKeyPair(userAlias: String): Pair<String, String> {
        val keyPairGenerator = KeyPairGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_RSA, KEYSTORE_PROVIDER
        )
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            userAlias,  // Each user gets a unique alias for their key
            KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY
        ).setDigests(KeyProperties.DIGEST_SHA256)
            .setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1)
            .build()

        keyPairGenerator.initialize(keyGenParameterSpec)
        val keyPair: KeyPair = keyPairGenerator.genKeyPair()

        val publicKey = Base64.getEncoder().encodeToString(keyPair.public.encoded)

        return Pair(publicKey, userAlias) // Return the public key and alias
    }

    fun getPrivateKey(userAlias: String): java.security.PrivateKey? {
        val keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER).apply { load(null) }
        val entry = keyStore.getEntry(userAlias, null) as? KeyStore.PrivateKeyEntry
        return entry?.privateKey
    }
}
