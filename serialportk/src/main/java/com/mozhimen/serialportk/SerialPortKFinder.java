/*
 * Copyright 2009 Cedric Priscal
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package com.mozhimen.serialportk;

import android.util.Log;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Iterator;
import java.util.Vector;

public class SerialPortKFinder {

    private static final String TAG = "SerialPortKFinder>>>>>";

    private Vector<SerialPortDriver> _serialPortDrivers = null;

    Vector<SerialPortDriver> getDrivers() throws IOException {
        if (_serialPortDrivers == null) {
            _serialPortDrivers = new Vector<>();
            LineNumberReader r = new LineNumberReader(new FileReader("/proc/tty/drivers"));
            String l;
            while ((l = r.readLine()) != null) {
                // Issue 3:
                // Since driver name may contain spaces, we do not extract driver name with split()
                String driverName = l.substring(0, 0x15).trim();
                String[] w = l.split(" +");
                if ((w.length >= 5) && (w[w.length - 1].equals("serial"))) {
                    Log.d(TAG, "Found new driver " + driverName + " on " + w[w.length - 4]);
                    _serialPortDrivers.add(new SerialPortDriver(driverName, w[w.length - 4]));
                }
            }
            r.close();
        }
        return _serialPortDrivers;
    }

    public String[] getAllDevices() {
        Vector<String> devices = new Vector<>();
        // Parse each driver
        Iterator<SerialPortDriver> iteratorDriver;
        try {
            iteratorDriver = getDrivers().iterator();
            while (iteratorDriver.hasNext()) {
                SerialPortDriver serialPortDriver = iteratorDriver.next();
                Iterator<File> iteratorDevices = serialPortDriver.getDevices().iterator();
                while (iteratorDevices.hasNext()) {
                    String device = iteratorDevices.next().getName();
                    String value = String.format("%s (%s)", device, serialPortDriver.getName());
                    devices.add(value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return devices.toArray(new String[devices.size()]);
    }

    public String[] getAllDevicesPath() {
        Vector<String> devices = new Vector<>();
        // Parse each driver
        Iterator<SerialPortDriver> iteratorDriver;
        try {
            iteratorDriver = getDrivers().iterator();
            while (iteratorDriver.hasNext()) {
                SerialPortDriver serialPortDriver = iteratorDriver.next();
                Iterator<File> iteratorDevice = serialPortDriver.getDevices().iterator();
                while (iteratorDevice.hasNext()) {
                    String device = iteratorDevice.next().getAbsolutePath();
                    devices.add(device);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return devices.toArray(new String[devices.size()]);
    }

    public class SerialPortDriver {
        private String _driverName;
        private String _deviceRoot;
        Vector<File> _devices = null;

        public SerialPortDriver(String name, String root) {
            _driverName = name;
            _deviceRoot = root;
        }

        public Vector<File> getDevices() {
            if (_devices == null) {
                _devices = new Vector<>();
                File dev = new File("/dev");

                File[] files = dev.listFiles();

                if (files != null) {
                    int i;
                    for (i = 0; i < files.length; i++) {
                        if (files[i].getAbsolutePath().startsWith(_deviceRoot)) {
                            Log.d(TAG, "Found new device: " + files[i]);
                            _devices.add(files[i]);
                        }
                    }
                }
            }
            return _devices;
        }

        public String getName() {
            return _driverName;
        }

        @Override
        public String toString() {
            return "SerialPortDriver{" +
                    "_driverName='" + _driverName + '\'' +
                    ", _deviceRoot='" + _deviceRoot + '\'' +
                    ", _devices=" + _devices +
                    '}';
        }
    }
}
