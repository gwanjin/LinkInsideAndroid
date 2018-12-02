package kita.example.com.kitkatproject;

import android.util.Log;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kita on 2017-01-09.
 */

public class NetworkConnect {
    public static StringBuilder send(String address, String data){
        StringBuilder result = null;
        try{
            URL url = new URL(address);

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            if(conn == null){
                Log.d("connection 상태", "연결 안됨");
            }else{
                Log.d("connection 상태", "연결 됨" + address);
            }

            if(conn != null){
                if(data != null){
                    conn.setUseCaches(false);
                    conn.setDoInput(true);  //  서버에서 읽기 모드
                    conn.setDoOutput(true); // 서버로 쓰기 모드
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");

                    OutputStreamWriter outStream = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
                    PrintWriter writer = new PrintWriter(outStream);
                    writer.write(data);
                    writer.flush();


                    InputStreamReader in = new InputStreamReader(conn.getInputStream());
                    // inputstram을 연결
                    int ch;
                    result = new StringBuilder();
                    while((ch = in.read()) != -1){
                        result.append((char)ch);
                    }

                }else{
                    conn.setConnectTimeout(10000);   // 연결 제한 시간. 0은 무한대기
                    conn.setUseCaches(false);        // 캐시 사용여부

                    // 연결을 요청해서 성공하면(200코드) 실행 => 서버에 정상적으로 연결됨
                    if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){ // 요청에 대한 응답코드 확인
                        InputStreamReader in = new InputStreamReader(conn.getInputStream());
                        // inputstram을 연결
                        int ch;
                        while((ch = in.read()) != -1){
                            result.append((char)ch);
                        }
                        in.close();

                    }
                }
            }

            conn.disconnect();

        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }
}
