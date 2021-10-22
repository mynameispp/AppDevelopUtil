package com.ffzxnet.developutil.utils.video_download.utils;

import android.text.TextUtils;

import com.ffzxnet.developutil.utils.video_download.model.VideoTaskItem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class KeyUtils {
    public static void checkKey(VideoTaskItem item) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String path=item.getSaveDir();
                if (TextUtils.isEmpty(path)){
                    path=item.getFilePath().substring(0,item.getFilePath().lastIndexOf("/"));
                }
                File localKey = new File(path + "/local_0.key");
                if (localKey.exists()) {
                    try {
                        FileReader fileReader = new FileReader(localKey);
                        BufferedReader bufferedReader = new BufferedReader(fileReader);
                        String keys = bufferedReader.readLine();
                        fileReader.close();
                        bufferedReader.close();
                        if (keys.length() != 16) {
                            File remoteM = new File(path + "/remote.m3u8");
                            FileReader remoteMfileReader = new FileReader(remoteM);
                            BufferedReader remoteMbufferedReader = new BufferedReader(remoteMfileReader);
                            String remoteKey=null;
                            for (String read = remoteMbufferedReader.readLine(); read != null; read = remoteMbufferedReader.readLine()) {
                                if (read.contains(".key")) {
                                    remoteKey=read.substring(read.indexOf("\"")+1,read.lastIndexOf("\""));
                                }
                            }
                            remoteMfileReader.close();
                            remoteMbufferedReader.close();
                            if (!TextUtils.isEmpty(remoteKey)) {
                                URL url = new URL(remoteKey);
                                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                connection.setRequestProperty("Charset","UTF-8");
                                connection.setRequestProperty("User-Agent","Mozilla/4.0(compatible;MSIE 5.0; Windows NT; DigExt)");
                                connection.setRequestMethod("GET");
                                Map<String, List<String>> map=connection.getHeaderFields();
                                for (Map.Entry<String, List<String>> stringListEntry : map.entrySet()) {
                                    if ("Location".equals(stringListEntry.getKey())){
                                        remoteKey=stringListEntry.getValue().get(0);
                                        break;
                                    }
                                }

                                if (connection.getResponseCode()==301){
                                    //重定向
                                    url = new URL(remoteKey);
                                    connection= (HttpURLConnection) url.openConnection();
                                }

                                InputStreamReader inputStreamReader=new InputStreamReader(connection.getInputStream());
                                BufferedReader remotString=new BufferedReader(inputStreamReader);

                                String keyVault=remotString.readLine();
                                FileOutputStream fileOutputStream=new FileOutputStream(localKey);
                                fileOutputStream.write(keyVault.getBytes());
                                connection.disconnect();
                                fileOutputStream.flush();
                                fileOutputStream.close();
                                remotString.close();
                                inputStreamReader.close();
                            }
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

}
