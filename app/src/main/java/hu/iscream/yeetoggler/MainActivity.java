package hu.iscream.yeetoggler;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.AlgorithmConstraints;
import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.toggleBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
    }

    private void toggle2() {
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    String uri = "https://cloud-de.yeelight.com/thirdparty/yeelight-android/control_old_devices";
                    String body = "{\"accesstoken\":\"V3_t40uFZs3JIDDhsD1vLLk8-3HgCOIhdz41nFxsokf04vikPRfdXYtMVMtnJH4kIyMNsDbNShHhW72ILfKURnknoQnhEZDaPq_XMWhmibSWDieqdCvBEqzD6Su2eqN5RDT\",\"bundleData\":[{\"did\":\"131154189\",\"command\":{\"method\":\"toggle\",\"params\":[]}}]}";
                    /*TrustStrategy acceptingTrustStrategy = new TrustStrategy() {
                        @Override
                        public boolean isTrusted(X509Certificate[] cert, String authType) throws CertificateException {
                            return true;
                        }
                    };
                    SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
                    SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);

                    Registry<ConnectionSocketFactory> socketFactoryRegistry =
                            RegistryBuilder.<ConnectionSocketFactory>create()
                                    .register("https", sslsf)
                                    .register("http", new PlainConnectionSocketFactory())
                                    .build();

                    BasicHttpClientConnectionManager connectionManager =
                            new BasicHttpClientConnectionManager(socketFactoryRegistry);
                    CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf)
                            .setConnectionManager(connectionManager).build();*/

                    URL url = new URL(uri);
                    HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                    httpsURLConnection.setRequestMethod("POST");
                    httpsURLConnection.setDoOutput(true);
                    BufferedOutputStream out = new BufferedOutputStream(httpsURLConnection.getOutputStream());

                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
                    writer.write(body);

                    writer.flush();
                    writer.close();
                    out.close();

                    try {
                        httpsURLConnection.connect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return null;

                    /*HttpPost post = new HttpPost(uri);

                    StringEntity entity = new StringEntity(body);
                    post.setEntity(entity);
                    HttpResponse resp = httpClient.execute(post);
                    httpClient.execute(HttpRequest)
                    return EntityUtils.toString(resp.getEntity());*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }


    private void toggle() {
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    String uri = "https://cloud-de.yeelight.com/thirdparty/yeelight-android/control_old_devices";
                    String body = "{\"accesstoken\":\"V3_t40uFZs3JIDDhsD1vLLk8-3HgCOIhdz41nFxsokf04vikPRfdXYtMVMtnJH4kIyMNsDbNShHhW72ILfKURnknoQnhEZDaPq_XMWhmibSWDieqdCvBEqzD6Su2eqN5RDT\",\"bundleData\":[{\"did\":\"131154189\",\"command\":{\"method\":\"toggle\",\"params\":[]}}]}";
                    SSLContext ctx;
                    ctx = SSLContext.getInstance("TLS");
                    ctx.init(new KeyManager[0], new TrustManager[]{new HttpsTrustManager()}, new SecureRandom());
                    SSLContext.setDefault(ctx);
                    HttpsURLConnection.setDefaultSSLSocketFactory(ctx.getSocketFactory());

                    URL serverUrl = new URL(uri);

                    HttpsURLConnection con = (HttpsURLConnection) serverUrl.openConnection();

                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    con.connect();

                    OutputStreamWriter post = new OutputStreamWriter(con.getOutputStream());
                    post.write(body);
                    post.flush();

                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    String content = "";
                    while ((inputLine = in.readLine()) != null) {
                        content += inputLine;
                    }
                    post.close();
                    in.close();

                    return content;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                showNotif(((String)o));
            }
        }.execute();
    }

    public class HttpsTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
            // TODO Auto-generated method stub

        }

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
            // TODO Auto-generated method stub

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showNotif(String notif) {
        String channelId = "idk";
        String channelName = "idk";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Battery percentage")
                .setContentText(notif + "")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setAutoCancel(true);

        final NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel mChannel = new NotificationChannel(
                channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(mChannel);

        final int notId = new Random().nextInt();
        notificationManager.notify(notId, builder.build());

        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                notificationManager.cancel(notId);
                return null;
            }
        }.execute();
    }
}
