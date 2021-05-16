package com.example.vetapp.utils;

import android.content.SharedPreferences;

import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Utils {

    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;
    public static final int PHONE_RESULT_CODE = 55;

    public static SharedPreferences mSettings;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_TOKEN = "token";
    public static final String APP_PREFERENCES_TOKEN_CLIENT = "token_client";
    public static final String APP_PREFERENCES_USERNAME = "username";
    public static final String APP_PREFERENCES_ID = "id";
    public static final String APP_PREFERENCES_CLIENT = "client";
    public static final String APP_PREFERENCES_CACHE_SCH = "sch_cache";

    public static final String LOCALE_HOST = "192.168.43.90:3000";

    public static final String CHECK_WORKER_EXISTS = "http://" + LOCALE_HOST +
            "/worker?login=eq.%s&select=password,salt,id";
    public static final String CHECK_CLIENT_EXISTS = "http://" + LOCALE_HOST +
            "/client?personal_code=eq.%s";
    public static final String GET_UNAVAILABLE_TIME = "http://" + LOCALE_HOST +
            "/schedule_of_visit?worker_id=eq.%s&date_and_time_of_visit=in.%s";
    public static final String GET_UPCOMING_VISITS = "http://" + LOCALE_HOST +
            "/rpc/return_only_upcoming_visits?id_=%s";
    public static final String GET_SCHEDULE_BY_CLIENT_ID = "http://" + LOCALE_HOST +
            "/schedule_of_visit?client_id=eq.%s";
    public static final String GET_TOKEN = "http://" + LOCALE_HOST + "/rpc/login?username=%s";
    public static final String GET_WORKERS_PHONES = "http://" + LOCALE_HOST + "/worker?select=phone";
    public static final String PUT_WORKER = "http://" + LOCALE_HOST + "/rpc/add_worker";
    public static final String GET_WORKER_BY_LOGIN = "http://" + LOCALE_HOST + "/worker?login=eq.%s";
    public static final String GET_WORKERS = "http://" + LOCALE_HOST + "/worker";
    public static final String GET_PETS = "http://" + LOCALE_HOST + "/pet";
    public static final String GET_DRUGS = "http://" + LOCALE_HOST + "/drug";
    public static final String GET_CLIENTS = "http://" + LOCALE_HOST + "/client";
    public static final String GET_SCHEDULE = "http://" + LOCALE_HOST + "/schedule_of_visit";
    public static final String GET_SERVICES = "http://" + LOCALE_HOST + "/service";
    public static final String GET_DRUG_REQUESTS = "http://" + LOCALE_HOST + "/drug_request";
    public static final String GET_DRUG = "http://" + LOCALE_HOST + "/drug";
    public static final String PUT_VISIT = "http://" + LOCALE_HOST + "/rpc/add_visit";
    public static final String PUT_PHONE = "http://" + LOCALE_HOST + "/rpc/change_phone";
    public static final String PUT_REQUEST = "http://" + LOCALE_HOST + "/rpc/add_request";
    public static final String PUT_PET = "http://" + LOCALE_HOST + "/rpc/add_pet";
    public static final String PUT_REQUEST_CLIENT_PET = "http://" + LOCALE_HOST +
            "/rpc/add_request_client_and_pet";
    public static final String CREATE_CLIENT_INFO = "http://" + LOCALE_HOST + "/client?id=eq.%s";
    public static final String CREATE_PET_INFO = "http://" + LOCALE_HOST + "/pet?client_id=eq.%s";

    public static final String WEIGHT_PATTERN = "([1-9]+[0-9]*)|(0\\.[1-9]+[0-9]*)";
    public static final String PHONE_PATTERN = "\\+7\\(9\\d{2}\\)\\d{3}-\\d{2}-\\d{2}";
    public static final String DOT_SPLIT = "\\.";
    public static final String DOT = ".";
    public static final String COLON_SPLIT = ":";
    public static final String T_SPLIT = "T";
    public static final String BRACKET = "}";
    public static final String DATE_FORMAT = "dd.MM.yyyy";
    public static final String HOUR_FORMAT = "HH:mm:ss";
    public static final String FUNCTION_RETURN = "ok";
    public static final String DEFAULT_PARAM = "the client did not provide a reason";
    public static final String ALPHABET = "abcdefghijklmnoqrstvuwxyz";
    public static final String CL = "cl";

    public static final String LOGIN_ERROR = "Uncorrected login or password";
    public static final String NETWORK_ERROR = "Network error";
    public static final String FULL_APPOINTMENT = "this specialist has a full appointment for the selected day";
    public static final String EMPTY_WARNING = "One or more of the field is empty" +
            "\n Fill in all the fields";
    public static final String GENDER_WARNING = "Gender should be male/female";
    public static final String AMOUNT_WARNING = "Amount must not be 0";
    public static final String DATE_ERROR = "Date not valid";
    public static final String LOGIN_CLIENT_ERROR = "Uncorrected code\nSuch client does not exist";
    public static final String ENTER_PHONE_ERROR = "Such number already exists\nEnter unique phone number";
    public static final String HASHING_ERROR = "Error while hashing a password: ";
    public static final String WEIGHT_WARNING = "Uncorrected weight";
    public static final String ERROR_REQUEST = "Error request";
    public static final String UNCORRECTED_PHONE = "Uncorrected phone\n" +
            "Please follow the pattern +7(9XX)XXX-XX-XX";

    public static final String CHANGE_PHONE = "Phone changed correctly";
    public static final String ADD_VISIT = "You have successfully made an appointment\n" +
            "It has been added to the list of your visits";
    public static final String ADD_PET = "Pet added successfully";
    public static final String ADD_DRUG_REQUEST = "Drug request added successfully";
    public static final String EMPTY = "";
    public static final String SPACE = " ";
    public static final String HYPHEN = "-";
    public static final String ANY = "any";
    public static final String TAG = "tag";
    public static final String MALE = "male";
    public static final String FEMALE = "female";
    public static final String NO_CACHE = "no cache";
    public static final String HAVE_A_CACHE = "caching";

    public static final String HEADER_PARAM_1 = "Content-Type";
    public static final String HEADER_PARAM_2 = "application/x-www-form-urlencoded";

    public static final String PET = "pet";
    public static final String PET_ID = "petId";
    public static final String PETS = "pets";
    public static final String PETS_ID = "petsId";
    public static final String SPECIALIST = "specialist";
    public static final String SPECIALIST_ID = "specialistId";
    public static final String DATE = "date";
    public static final String TIME = "time";
    public static final String REASON = "reason";

    public static byte[] getHash(char[] password, byte[] salt) {
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
        Arrays.fill(password, Character.MIN_VALUE);
        try {
            final SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return skf.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException | java.security.NoSuchAlgorithmException e) {
            throw new AssertionError(HASHING_ERROR + e.getMessage(), e);
        } finally {
            spec.clearPassword();
        }
    }

    public static String generatePersonalCode(int amount, String str) {
        StringBuilder password = new StringBuilder();
        for (int i = 0; i <= amount; i++) {
            if (Utils.getRand(0, 2) == 1) {
                password.append(str.charAt(new Random().nextInt(str.length() - 1)));
            } else {
                password.append(Utils.getRand(0,10));
            }
        }
        return password.toString();
    }

    public static int getRand(int a, int b) {
        return (int) (Math.random() * (b-a)) + a;
    }

    public static String formatDateForDB(String date) {
        String[] split_date = date.split(DOT_SPLIT);
        return split_date[2] + HYPHEN + split_date[1] + HYPHEN + split_date[0];
    }

    public static String formatDateForUI(String date) {
        String[] split_date = date.split(HYPHEN);
        return split_date[2] + DOT + split_date[1] + DOT + split_date[0];
    }

}
