package com.mozhimen.pidk.serialport.helpers


/**
 * @ClassName CRCKermit
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2023/1/31 19:10
 * @Version 1.0
 */
object CRCKermit {
    var crc_tab = ShortArray(256)
    var is_init = false

    @JvmStatic
    fun init_crc_tab() {
        var j: Short
        var crc: Int
        var c: Int
        var i: Short = 0
        while (i < 256) {
            crc = 0
            c = i.toInt()
            j = 0
            while (j < 8) {
                crc = if (crc xor c and 0x0001 != 0) {
                    crc shr 1 xor 0x8408
                } else {
                    crc shr 1
                }
                c = c shr 1
                j++
            }
            crc_tab[i.toInt()] = crc.toShort()
            i++
        }
        is_init = true
    }

    @JvmStatic
    fun crc_kermit(input_byte: ByteArray): Int {
        var crc: Int
        var tmp: Int
        var short_c: Int
        if (!is_init) {
            init_crc_tab()
        }
        crc = 0x0000
        var a = 0
        while (a < input_byte.size) {
            short_c = 0x00ff and input_byte[a].toInt()
            tmp = (crc xor short_c)
            if (tmp < 0) {
                tmp += 65536
            }
            crc = (crc shr 8 xor crc_tab[tmp and 0xff].toInt())
            if (crc < 0) {
                crc += 65536
            }
            a++
        }
        val low_byte: Int = crc and 0xff00 shr 8
        val high_byte: Int = crc and 0x00ff shl 8
        crc = low_byte or high_byte
        return crc
    }
}