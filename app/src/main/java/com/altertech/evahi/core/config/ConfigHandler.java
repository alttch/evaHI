package com.altertech.evahi.core.config;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.altertech.evahi.AppConfig;
import com.altertech.evahi.core.exception.CustomException;
import com.altertech.evahi.core.parser.SingletonMapper;
import com.altertech.evahi.utils.ImageUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.SSLHandshakeException;

/**
 * Created by oshevchuk on 14.02.2019
 */
public class ConfigHandler {

    private final CallBack
            listener;

    private final String username;
    private final String password;
    private final String url;

    public ConfigHandler(String url, String username, String password, CallBack listener) {
        this.listener = listener;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public void load() {
         new LOADER().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public interface CallBack {
        void start();

        void end(Config config);

        void error(CustomException e);
    }

    @SuppressLint("StaticFieldLeak")
    private class LOADER extends AsyncTask<Void, Void, Object> {

        @Override
        protected void onPreExecute() {
            ConfigHandler.this.listener.start();
        }

        @Override
        protected Object doInBackground(Void... voids) {
            try {
                return new Result(ConfigHandler.this.prepare(ConfigHandler.this.fromYaml(ConfigHandler.this.getFromUrl(url, "/.evahi/config.yml")).valid()));
            } catch (CustomException yamlE) {
                if (yamlE.getError().equals(CustomException.Error.NO_CONNECTION_TO_SERVER) || yamlE.getError().equals(CustomException.Error.CONNECTION_ERROR_HAND_SHAKE)) {
                    return yamlE;
                } else {
                    try {
                        return new Result(ConfigHandler.this.prepare(ConfigHandler.this.fromJson(ConfigHandler.this.getFromUrl(url, "/.evahi/config.json")).valid()));
                    } catch (CustomException jsonE) {
                        return jsonE;
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(Object result) {
            if (result instanceof Result) {
                ConfigHandler.this.listener.end(((Result) result).config);
            } else {
                ConfigHandler.this.listener.error((CustomException) result);
            }
        }

        private class Result {
            Config config;

            Result(Config config) {
                this.config = config;
            }
        }
    }

    private Config prepare(Config config) {
        if (config != null) {
            config.setHome_icon(getImage(config.getHome_icon()));
            if (config.getMenu() != null && !config.getMenu().isEmpty()) {
                for (Menu menu : config.getMenu()) {
                    menu.setIcon(getImage(menu.getIcon()));
                }
            }
            return config;
        } else {
            return null;
        }
    }

    private String getImage(String url) {
        if (url == null || url.length() == 0) {
            return null;
        } else {
            try {
                return ImageUtil.convert(BitmapFactory.decodeStream(new URL(new URL(this.url), ("/.evahi/icons/" + url)).openConnection().getInputStream()));
            } catch (IOException e) {
                return null;
            }
        }
    }

    private Config fromYaml(String config) throws CustomException {
        if (config == null || config.length() == 0) {
            throw new CustomException(CustomException.Error.FILE_EMPTY);
        } else {
            try {
                return SingletonMapper.getInstanceYaml().readValue(config, Config.class);
            } catch (Exception e) {
                throw new CustomException(CustomException.Error.PARSE_ERROR);
            }
        }
    }

    private Config fromJson(String config) throws CustomException {
        if (config == null || config.length() == 0) {
            throw new CustomException(CustomException.Error.FILE_EMPTY);
        } else {
            try {
                return SingletonMapper.getInstanceJson().readValue(config, Config.class);
            } catch (Exception e) {
                throw new CustomException(CustomException.Error.PARSE_ERROR);
            }
        }
    }

    private String getFromUrl(String base, String u) throws CustomException {
        if (base == null || u == null || base.length() == 0 || u.length() == 0) {
            throw new CustomException(CustomException.Error.BAD_URL);
        } else {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) new URL(new URL(base), u).openConnection();
                if (!AppConfig.AUTHENTICATION && username != null && !username.isEmpty()) {
                    connection.setRequestProperty("Authorization", "Basic " + android.util.Base64.encodeToString(String.format("%s:%s", username, password).getBytes(), android.util.Base64.NO_WRAP));
                }
                connection.setConnectTimeout(5000);
                InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                StringBuilder result = new StringBuilder();
                int data = reader.read();
                while (data != -1) {
                    result.append((char) data);
                    data = reader.read();
                }
                String output = result.toString();
                if (output.length() == 0) {
                    throw new CustomException(CustomException.Error.FILE_EMPTY);
                } else {
                    return output;
                }
            } catch (MalformedURLException e) {
                throw new CustomException(CustomException.Error.BAD_URL);
            } catch (FileNotFoundException e) {
                throw new CustomException(CustomException.Error.FILE_NOT_FOUND);
            } catch (SSLHandshakeException e) {
                throw new CustomException(CustomException.Error.CONNECTION_ERROR_HAND_SHAKE);
            } catch (IOException e) {
                throw new CustomException(CustomException.Error.NO_CONNECTION_TO_SERVER);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
    }
}