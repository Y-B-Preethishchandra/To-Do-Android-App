package com.todo.ybpreethishchandra.todo

import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class Encode_Decode(key:ByteArray) {
    var key:ByteArray = key
    var ALGORITHM="AES"

    fun encrypt(password:ByteArray): ByteArray {
        var secretKey: SecretKeySpec = SecretKeySpec(key,ALGORITHM)
        var cipher= Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE,secretKey)
        return  cipher.doFinal(password)
    }

    fun decrypt(ciphertext: ByteArray): ByteArray {
        var secretKey: SecretKeySpec = SecretKeySpec(key,ALGORITHM)
        var cipher= Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE,secretKey)
        return cipher.doFinal(ciphertext)
    }
}