package com.example.utils.app;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apkinfo.api.util.AXmlResourceParser;
import org.apkinfo.api.util.TypedValue;
import org.apkinfo.api.util.XmlPullParser;


/**
 * Created by yuhanxun on 16/1/4.
 */
public class ApkInfo {
    public static String getVersionName(Context context) {
        String ret = null;
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            ret = info.versionName;
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }
        return ret;
    }
    public static int getVersionCode(Context context) {
        int ret = -1;
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            ret = info.versionCode;
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }
        return ret;
    }

    public static String getAppPackage(Context context) {
        return context.getPackageName();
    }

    public static String getMetaData(Context context, String key) {
        String ret = null;
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            ret = String.valueOf(appInfo.metaData.get(key));
//            ret = appInfo.metaData.getString(key);
//            if(ret == null) {
//                ret =String.valueOf(appInfo.metaData.getInt(key));
//            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Log.d("ApkInfo_xunxun","getMetaData key:"+key+" ret:"+ret);
        return ret;
    }


    public static APKFileUtils.APKFileInfo getApkFileInfo(String apkPath) {
        APKFileUtils apkFileUtils = new APKFileUtils();
        Map<String, Object> mapApk = apkFileUtils.readAPK(apkPath);
        APKFileUtils.APKFileInfo ret = new APKFileUtils.APKFileInfo();
        ret.packageName = mapApk.get("package").toString();
        ret.versionCode = mapApk.get("versionCode").toString();
        ret.versionName = mapApk.get("versionName").toString();
        return ret;
    }

    public static void main(String[]args){
        String url = "/Users/dbstar-mac/source/hotel/DBStarSmartHotel/build/apk/DataSync-release_v1.0.apk";
        APKFileUtils.APKFileInfo ret = getApkFileInfo(url);
        System.out.print(ret.toString());
    }



    public static class APKFileUtils {
        /**
         * 读取apk
         *
         * @param apkUrl
         * @return
         */
        private Map<String, Object> readAPK(String apkUrl) {
            ZipFile zipFile;
            Map<String, Object> map = new HashMap<String, Object>();
            try {
                zipFile = new ZipFile(apkUrl);
                Enumeration<?> enumeration = zipFile.entries();
                ZipEntry zipEntry = null;
                while (enumeration.hasMoreElements()) {
                    zipEntry = (ZipEntry) enumeration.nextElement();
                    if (zipEntry.isDirectory()) {

                    } else {
                        if ("androidmanifest.xml".equals(zipEntry.getName().toLowerCase())) {
                            AXmlResourceParser parser = new AXmlResourceParser();
                            parser.open(zipFile.getInputStream(zipEntry));
                            while (true) {
                                int type = parser.next();
                                if (type == XmlPullParser.END_DOCUMENT) {
                                    break;
                                }
                                String name = parser.getName();
                                if (null != name && name.toLowerCase().equals("manifest")) {
                                    for (int i = 0; i != parser.getAttributeCount(); i++) {
                                        if ("versionName".equals(parser.getAttributeName(i))) {
                                            String versionName = getAttributeValue(parser, i);
                                            if (null == versionName) {
                                                versionName = "";
                                            }
                                            map.put("versionName", versionName);
                                        } else if ("package".equals(parser.getAttributeName(i))) {
                                            String packageName = getAttributeValue(parser, i);
                                            if (null == packageName) {
                                                packageName = "";
                                            }
                                            map.put("package", packageName);
                                        } else if ("versionCode".equals(parser.getAttributeName(i))) {
                                            String versionCode = getAttributeValue(parser, i);
                                            if (null == versionCode) {
                                                versionCode = "";
                                            }
                                            map.put("versionCode", versionCode);
                                        }
                                    }
                                    break;
                                }
                            }
                        }

                    }
                }
                zipFile.close();
            } catch (Exception e) {
                map.put("code", "fail");
                map.put("error", "读取apk失败");
            }
            return map;
        }

        private String getAttributeValue(AXmlResourceParser parser, int index) {
            int type = parser.getAttributeValueType(index);
            int data = parser.getAttributeValueData(index);
            if (type == TypedValue.TYPE_STRING) {
                return parser.getAttributeValue(index);
            }
            if (type == TypedValue.TYPE_ATTRIBUTE) {
                return String.format("?%s%08X", getPackage(data), data);
            }
            if (type == TypedValue.TYPE_REFERENCE) {
                return String.format("@%s%08X", getPackage(data), data);
            }
            if (type == TypedValue.TYPE_FLOAT) {
                return String.valueOf(Float.intBitsToFloat(data));
            }
            if (type == TypedValue.TYPE_INT_HEX) {
                return String.format("0x%08X", data);
            }
            if (type == TypedValue.TYPE_INT_BOOLEAN) {
                return data != 0 ? "true" : "false";
            }
            if (type == TypedValue.TYPE_DIMENSION) {
                return Float.toString(complexToFloat(data)) + DIMENSION_UNITS[data & TypedValue.COMPLEX_UNIT_MASK];
            }
            if (type == TypedValue.TYPE_FRACTION) {
                return Float.toString(complexToFloat(data)) + FRACTION_UNITS[data & TypedValue.COMPLEX_UNIT_MASK];
            }
            if (type >= TypedValue.TYPE_FIRST_COLOR_INT && type <= TypedValue.TYPE_LAST_COLOR_INT) {
                return String.format("#%08X", data);
            }
            if (type >= TypedValue.TYPE_FIRST_INT && type <= TypedValue.TYPE_LAST_INT) {
                return String.valueOf(data);
            }
            return String.format("<0x%X, type 0x%02X>", data, type);
        }

        private String getPackage(int id) {
            if (id >>> 24 == 1) {
                return "android:";
            }
            return "";
        }

        // ///////////////////////////////// ILLEGAL STUFF, DONT LOOK :)
        public float complexToFloat(int complex) {
            return (float) (complex & 0xFFFFFF00) * RADIX_MULTS[(complex >> 4) & 3];
        }

        private final float RADIX_MULTS[] =
                {
                        0.00390625F, 3.051758E-005F,
                        1.192093E-007F, 4.656613E-010F
                };
        private final String DIMENSION_UNITS[] = {"px", "dip", "sp", "pt", "in", "mm", "", ""};
        private final String FRACTION_UNITS[] = {"%", "%p", "", "", "", "", "", ""};

        public static class APKFileInfo {
            public String packageName;
            public String versionName;
            public String versionCode;

            @Override
            public String toString() {
                return "APKFileInfo{" +
                        "packageName='" + packageName + '\'' +
                        ", versionName='" + versionName + '\'' +
                        ", versionCode='" + versionCode + '\'' +
                        '}';
            }
        }
    }
}
