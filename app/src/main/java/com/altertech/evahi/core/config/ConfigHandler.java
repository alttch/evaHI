package com.altertech.evahi.core.config;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.altertech.evahi.core.exception.CustomException;
import com.altertech.evahi.core.parser.SingletonMapper;
import com.altertech.evahi.helpers.TaskHelper;
import com.altertech.evahi.utils.ImageUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by oshevchuk on 14.02.2019
 */
public class ConfigHandler {

    private Config old;

    private CallBack listener;

    private String url;

    public ConfigHandler(String url, Config old, CallBack listener) {
        this.old = old;
        this.listener = listener;
        this.url = url;
    }

    public void load() {
        TaskHelper.execute(new LOADER());
    }

    public interface CallBack {
        void start();

        void end(boolean updated, Config config);

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
                Config config = ConfigHandler.this.fromYaml(ConfigHandler.this.getFromUrl(url + "/.evahi/config.yml"));
                if (ConfigHandler.this.resolveToUpdate(config)) {
                    return new Result(true, ConfigHandler.this.prepare(config));
                } else {
                    return new Result(false, ConfigHandler.this.old);
                }
            } catch (CustomException yamlE) {
                if (yamlE.getCode().equals(CustomException.Code.NO_CONNECTION_TO_SERVER)) {
                    return yamlE;
                } else {
                    try {
                        Config config = ConfigHandler.this.fromJson(ConfigHandler.this.getFromUrl(url + "/.evahi/config.json"));
                        if (ConfigHandler.this.resolveToUpdate(config)) {
                            return new Result(true, ConfigHandler.this.prepare(config));
                        } else {
                            return new Result(false, ConfigHandler.this.old);
                        }
                    } catch (CustomException jsonE) {
                        return jsonE;
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(Object result) {
            if (result instanceof Result) {
                ConfigHandler.this.listener.end(((Result) result).updated, ((Result) result).config);
            } else {
                ConfigHandler.this.listener.error((CustomException) result);
            }
        }

        private class Result {
            boolean updated;
            Config config;

            public Result(boolean updated, Config config) {
                this.updated = updated;
                this.config = config;
            }
        }
    }

    private boolean resolveToUpdate(Config config) {
        return this.old == null || this.old.getVersion() != config.getVersion();
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
                return ImageUtil.convert(BitmapFactory.decodeStream(new URL(this.url + "/.evahi/icons/" + url).openConnection().getInputStream()));
            } catch (IOException e) {
                return null;
            }
        }
    }

    private Config fromYaml(String config) throws CustomException {
        if (config == null || config.length() == 0) {
            throw new CustomException(CustomException.Code.FILE_EMPTY);
        } else {
            try {
                return SingletonMapper.getInstanceYaml().readValue(config, Config.class);
            } catch (Exception e) {
                throw new CustomException(CustomException.Code.PARSE_ERROR);
            }
        }
    }

    private Config fromJson(String config) throws CustomException {
        if (config == null || config.length() == 0) {
            throw new CustomException(CustomException.Code.FILE_EMPTY);
        } else {
            try {
                return SingletonMapper.getInstanceJson().readValue(config, Config.class);
            } catch (Exception e) {
                throw new CustomException(CustomException.Code.PARSE_ERROR);
            }
        }
    }

    private String getFromUrl(String u) throws CustomException {
        if (u == null || u.length() == 0) {
            throw new CustomException(CustomException.Code.BAD_URL);
        } else {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) new URL(u).openConnection();

                InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                StringBuilder result = new StringBuilder();
                int data = reader.read();
                while (data != -1) {
                    result.append((char) data);
                    data = reader.read();
                }
                String output = result.toString();
                if (output.length() == 0) {
                    throw new CustomException(CustomException.Code.FILE_EMPTY);
                } else {
                    return output;
                }
            } catch (MalformedURLException e) {
                throw new CustomException(CustomException.Code.BAD_URL);
            } catch (FileNotFoundException e) {
                throw new CustomException(CustomException.Code.FILE_NOT_FOUND);
            } catch (IOException e) {
                throw new CustomException(CustomException.Code.NO_CONNECTION_TO_SERVER);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
    }
}








