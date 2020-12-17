package com.example.utils.command;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandUtil {
	// 对结果进行处理，得到自己想要的数据
	public static String do_command(String cmd) {
		String ret = "";
		// 根据自己的需求，制定正则表达式
		String match = "\\d+";
		Pattern p = Pattern.compile(match);
		Matcher m = p.matcher(cmd);
		if (m.find()) {
			String arr = m.group(0);
			String cmd2 = m.replaceFirst("@");
			ret = arr + "@" + do_command(cmd2);
		}
		return ret;
	}

    public static void root() {
        try {
            Runtime.getRuntime().exec("/system/xbin/su");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public static void rootCommand(String command) {
		Process process = null;
		DataOutputStream os = null;
		DataInputStream is = null;
		try {
			process = Runtime.getRuntime().exec("/system/xbin/su"); //这里可能需要修改su的源代码 （注掉  if (myuid != AID_ROOT && myuid != AID_SHELL) {）

			os = new DataOutputStream(process.getOutputStream());
			is = new DataInputStream(process.getInputStream());
			os.writeBytes(command + " \n");       //这里可以执行具有root 权限的程序了
			os.writeBytes(" exit \n");
			os.flush();
			process.waitFor();

		} catch (Exception e) {
			Log.e("yhx", "Unexpected error - Here is what I know:" + e.getMessage());
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				if (is != null) {
					is.close();
				}
				process.destroy();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}// getJSONObjFromFile the root privileges
	}

	public static void forceStop(String packageName){
		String command = "/system/bin/am force-stop "+packageName;
		rootCommand(command);
	}

	public static void reboot() {
		rootCommand("/system/bin/reboot");
	}

	public static void shutdown() {
		rootCommand("/system/bin/shutdown");
	}

	public static synchronized String run(String[] cmd, String workdirectory) throws IOException {
		StringBuffer result = new StringBuffer();
		try {
			// 创建操作系统进程（也可以由Runtime.exec()启动）
			// Runtime runtime = Runtime.getRuntime();
			// Process proc = runtime.exec(cmd);
			// InputStream inputstream = proc.getInputStream();
			ProcessBuilder builder = new ProcessBuilder(cmd);
			InputStream in = null;
			// 设置一个路径（绝对路径了就不一定需要）
			if (workdirectory != null) {
				// 设置工作目录（同上）
				builder.directory(new File(workdirectory));
				// 合并标准错误和标准输出
				builder.redirectErrorStream(true);
				// 启动一个新进程
				Process process = builder.start();
				// 读取进程标准输出流
				in = process.getInputStream();
				byte[] re = new byte[1024];
				while (in.read(re) != -1) {
					result = result.append(new String(re));
				}
			}
			// 关闭输入流
			if (in != null) {
				in.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result.toString();
	}
}
