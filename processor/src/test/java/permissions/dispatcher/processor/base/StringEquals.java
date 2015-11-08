package permissions.dispatcher.processor.base;

import org.hamcrest.core.SubstringMatcher;

final class StringEquals extends SubstringMatcher {

    protected StringEquals(String substring) {
        super(substring);
    }

    @Override
    protected boolean evalSubstringOf(String string) {
        // Strip Exception prefix from the string to only include the actual message
        string = string.substring(string.indexOf(':') + 2);
        return substring != null && substring.equals(string);
    }

    @Override
    protected String relationship() {
        return "equals";
    }
}
