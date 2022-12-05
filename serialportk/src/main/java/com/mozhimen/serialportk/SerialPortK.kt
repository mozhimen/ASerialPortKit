package com.mozhimen.serialportk

import android.util.Log
import java.io.*


/**
 * @ClassName SerialPortK
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2022/12/5 15:42
 * @Version 1.0
 */
class SerialPortK {
    companion object {
        private const val TAG = "SerialPortK>>>>>"
        const val DEFAULT_SU_PATH = "/system/bin/su"
        private var _fd: FileDescriptor? = null//Do not remove or rename the field _fd: it is used by native method close();

        init {
            System.loadLibrary("serialportk")
        }

        // JNI
        external fun open(
            absolutePath: String, baudrate: Int, dataBits: Int, parity: Int,
            stopBits: Int, flags: Int
        ): FileDescriptor?

        external fun close()

        fun newBuilder(device: File, baudrate: Int): Builder {
            return Builder(device, baudrate)
        }

        fun newBuilder(devicePath: String, baudrate: Int): Builder {
            return Builder(devicePath, baudrate)
        }
    }

    private var _device: File? = null
    private var _sSuPath = DEFAULT_SU_PATH
    private var _baudrate = 0
    private var _dataBits = 0
    private var _parity = 0
    private var _stopBits = 0
    private var _flags = 0
    private var _fileInputStream: FileInputStream? = null
    private var _fileOutputStream: FileOutputStream? = null

    /**
     * 串口
     * @param device 串口设备文件
     * @param baudrate 波特率
     * @param dataBits 数据位；默认8,可选值为5~8
     * @param parity 奇偶校验；0:无校验位(NONE，默认)；1:奇校验位(ODD);2:偶校验位(EVEN)
     * @param stopBits 停止位；默认1；1:1位停止位；2:2位停止位
     * @param flags 默认0
     * @throws SecurityException
     * @throws IOException
     */
    @Throws(SecurityException::class, IOException::class)
    constructor(
        device: File, baudrate: Int, dataBits: Int, parity: Int, stopBits: Int,
        flags: Int
    ) {
        this._device = device
        this._baudrate = baudrate
        this._dataBits = dataBits
        this._parity = parity
        this._stopBits = stopBits
        this._flags = flags

        /* Check access permission */if (!device.canRead() || !device.canWrite()) {
            try {
                /* Missing read/write permission, trying to chmod the file */
                val su: Process = Runtime.getRuntime().exec(_sSuPath)
                val cmd = """
                chmod 666 ${device.absolutePath}
                exit
                
                """.trimIndent()
                su.outputStream.write(cmd.toByteArray())
                if (su.waitFor() != 0 || !device.canRead() || !device.canWrite()) {
                    throw SecurityException()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                throw SecurityException()
            }
        }
        _fd = open(device.absolutePath, baudrate, dataBits, parity, stopBits, flags)
        if (_fd == null) {
            Log.e(TAG, "native open returns null")
            throw IOException()
        }
        _fileInputStream = FileInputStream(_fd)
        _fileOutputStream = FileOutputStream(_fd)
    }

    /**
     * 串口，默认的8n1
     * @param device 串口设备文件
     * @param baudrate 波特率
     * @throws SecurityException
     * @throws IOException
     */
    @Throws(SecurityException::class, IOException::class)
    constructor(device: File, baudrate: Int) : this(device, baudrate, 8, 0, 1, 0)

    /**
     * 串口
     * @param device 串口设备文件
     * @param baudrate 波特率
     * @param dataBits 数据位；默认8,可选值为5~8
     * @param parity 奇偶校验；0:无校验位(NONE，默认)；1:奇校验位(ODD);2:偶校验位(EVEN)
     * @param stopBits 停止位；默认1；1:1位停止位；2:2位停止位
     * @throws SecurityException
     * @throws IOException
     */
    @Throws(SecurityException::class, IOException::class)
    constructor(device: File, baudrate: Int, dataBits: Int, parity: Int, stopBits: Int) : this(device, baudrate, dataBits, parity, stopBits, 0)

    // Getters and setters
    /**
     * Set the su binary path, the default su binary path is [.DEFAULT_SU_PATH]
     * @param suPath su binary path
     */
    fun setSuPath(suPath: String) {
        _sSuPath = suPath
    }

    /**
     * Get the su binary path
     * @return
     */
    fun getSuPath(): String {
        return _sSuPath
    }

    fun getInputStream(): InputStream {
        return _fileInputStream!!
    }

    fun getOutputStream(): OutputStream {
        return _fileOutputStream!!
    }

    /** 串口设备文件  */
    fun getDevice(): File {
        return _device!!
    }

    /** 波特率  */
    fun getBaudrate(): Int {
        return _baudrate
    }

    /** 数据位；默认8,可选值为5~8  */
    fun getDataBits(): Int {
        return _dataBits
    }

    /** 奇偶校验；0:无校验位(NONE，默认)；1:奇校验位(ODD);2:偶校验位(EVEN)  */
    fun getParity(): Int {
        return _parity
    }

    /** 停止位；默认1；1:1位停止位；2:2位停止位  */
    fun getStopBits(): Int {
        return _stopBits
    }

    fun getFlags(): Int {
        return _flags
    }

    /** 关闭流和串口，已经try-catch  */
    fun tryClose() {
        try {
            _fileInputStream!!.close()
        } catch (e: IOException) {
            //e.printStackTrace();
        }
        try {
            _fileOutputStream!!.close()
        } catch (e: IOException) {
            //e.printStackTrace();
        }
        try {
            close()
        } catch (e: Exception) {
            //e.printStackTrace();
        }
    }

    class Builder constructor(private val device: File, private val baudrate: Int) {
        private var _dataBits = 8
        private var _parity = 0
        private var _stopBits = 1
        private var _flags = 0

        constructor(devicePath: String, baudrate: Int) : this(File(devicePath), baudrate)

        /**
         * 数据位
         * @param dataBits 默认8,可选值为5~8
         * @return
         */
        fun setDataBits(dataBits: Int): Builder {
            this._dataBits = dataBits
            return this
        }

        /**
         * 校验位
         * @param parity 0:无校验位(NONE，默认)；1:奇校验位(ODD);2:偶校验位(EVEN)
         * @return
         */
        fun setParity(parity: Int): Builder {
            this._parity = parity
            return this
        }

        /**
         * 停止位
         * @param stopBits 默认1；1:1位停止位；2:2位停止位
         * @return
         */
        fun setStopBits(stopBits: Int): Builder {
            this._stopBits = stopBits
            return this
        }

        /**
         * 标志
         * @param flags 默认0
         * @return
         */
        fun setFlags(flags: Int): Builder {
            this._flags = flags
            return this
        }

        /**
         * 打开并返回串口
         * @return
         * @throws SecurityException
         * @throws IOException
         */
        @Throws(SecurityException::class, IOException::class)
        fun build(): SerialPortK {
            return SerialPortK(device, baudrate, _dataBits, _parity, _stopBits, _flags)
        }
    }
}