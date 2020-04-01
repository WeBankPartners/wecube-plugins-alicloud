package com.webank.wecube.plugins.alicloud.support.password;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author howechen
 */
public interface PasswordGenerator {
    char[] ALLOWED_CHAR_ARR = {'(', ')', '`', '~', '!', '@', '#', '$', '%', '^', '&', '*', '-', '_', '+', '=', '|', '{', '}', '[', ']', ':', ';', '\'', '<', '>', ',', '.', '?', '/'};

    /**
     * Password generation
     *
     * @return generated password
     */
    static String generatePassword() {
        // lower letter + upper letter = 15
        final int lowerLetterCount = RandomUtils.nextInt(1, 15);
        final int upperLetterCount = 15 - lowerLetterCount;
        // number + character = 15
        final int numberCount = RandomUtils.nextInt(1, 15);
        final int characterCount = 15 - numberCount;

        // password generation
        String upperCaseLetters = RandomStringUtils.random(lowerLetterCount, 65, 90, true, false);
        String lowerCaseLetters = RandomStringUtils.random(upperLetterCount, 97, 122, true, false);
        String numbers = RandomStringUtils.randomNumeric(numberCount);
        String specialChar = RandomStringUtils.random(characterCount, 0, 0, false, false, ALLOWED_CHAR_ARR);

        String rdmString = upperCaseLetters.concat(lowerCaseLetters)
                .concat(numbers)
                .concat(specialChar);
        List<Character> pwdChars = rdmString.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());

        do {
            Collections.shuffle(pwdChars);
        } while (pwdChars.get(0) == '/');

        return pwdChars.stream()
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }
}
