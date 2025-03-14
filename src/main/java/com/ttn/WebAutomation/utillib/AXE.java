package com.ttn.WebAutomation.utillib;

/**
 * This Java program demonstrates Axe code for accessibility testing.
 *
 * @author TTN
 */

import com.ttn.WebAutomation.seleniumUtils.SeleniumHelper;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AXE {
    protected static Logger logger = LoggerFactory.getLogger(SeleniumHelper.class);

    private static final String lineSeparator = System.getProperty("line.separator");

    /**
     * @return Contents of the axe.js or axe.min.js script with a configured reporter.
     */
    private static String getContents(final URL script) {
        final StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;

        try {
            URLConnection connection = script.openConnection();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append(lineSeparator);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {
                }
            }
        }

        return sb.toString();
    }

    /**
     * Recursively injects a script to the top level document with the option to skip iframes.
     *
     * @param driver     WebDriver instance to inject into
     * @param scriptUrl  URL to the script to inject
     * @param skipFrames True if the script should not be injected into iframes
     */
    public static void inject(final WebDriver driver, final URL scriptUrl, Boolean skipFrames) {
        final String script = getContents(scriptUrl);

        if (!skipFrames) {
            final ArrayList<WebElement> parents = new ArrayList<WebElement>();
            injectIntoFrames(driver, script, parents);
        }

        JavascriptExecutor js = (JavascriptExecutor) driver;
        driver.switchTo().defaultContent();
        js.executeScript(script);
    }


    /**
     * Recursively find frames and inject a script into them.
     *
     * @param driver  An initialized WebDriver
     * @param script  Script to inject
     * @param parents A list of all toplevel frames
     */
    private static void injectIntoFrames(final WebDriver driver, final String script, final ArrayList<WebElement> parents) {
        final JavascriptExecutor js = (JavascriptExecutor) driver;
        final List<WebElement> frames = driver.findElements(By.tagName("iframe"));

        for (WebElement frame : frames) {
            driver.switchTo().defaultContent();

            if (parents != null) {
                for (WebElement parent : parents) {
                    driver.switchTo().frame(parent);
                }
            }

            driver.switchTo().frame(frame);
            js.executeScript(script);
            try {
                assert parents != null;
                @SuppressWarnings("unchecked")
                ArrayList<WebElement> localParents = (ArrayList<WebElement>) parents.clone();

                localParents.add(frame);

                injectIntoFrames(driver, script, localParents);
            } catch (NullPointerException e) {
                logger.info("NullPointerException", e);
            }
        }

    }

    /**
     * @param violations JSONArray of violations
     * @return readable report of accessibility violations found
     */
    public static String report(final JSONArray violations) throws JSONException {
        final StringBuilder sb = new StringBuilder();
        sb
                .append("Found ")
                .append(violations.length())
                .append(" accessibility violations:");

        for (int i = 0; i < violations.length(); i++) {
            JSONObject violation = violations.getJSONObject(i);
            sb
                    .append(lineSeparator)
                    .append(i + 1)
                    .append(") ")
                    .append(violation.getString("help"));

            if (violation.has("helpUrl")) {
                String helpUrl = violation.getString("helpUrl");
                sb.append(": ")
                        .append(helpUrl);
            }

            JSONArray nodes = violation.getJSONArray("nodes");

            for (int j = 0; j < nodes.length(); j++) {
                JSONObject node = nodes.getJSONObject(j);
                sb
                        .append(lineSeparator)
                        .append("  ")
                        .append(getOrdinal(j + 1))
                        .append(") ")
                        .append(node.getJSONArray("target"))
                        .append(lineSeparator);

                JSONArray all = node.getJSONArray("all");
                JSONArray none = node.getJSONArray("none");

                for (int k = 0; k < none.length(); k++) {
                    all.put(none.getJSONObject(k));
                }

                appendFixes(sb, all, "Fix all of the following:");
                appendFixes(sb, node.getJSONArray("any"), "Fix any of the following:");
            }
        }

        return sb.toString();
    }

    private static void appendFixes(final StringBuilder sb, final JSONArray arr, final String heading) throws JSONException {
        if (arr != null && arr.length() > 0) {
            sb
                    .append("    ")
                    .append(heading)
                    .append(lineSeparator);

            for (int i = 0; i < arr.length(); i++) {
                JSONObject fix = arr.getJSONObject(i);

                sb
                        .append("      ")
                        .append(fix.get("message"))
                        .append(lineSeparator);
            }

            sb.append(lineSeparator);
        }
    }

    private static String getOrdinal(int number) {
        String ordinal = "";

        int mod;

        while (number > 0) {
            mod = (number - 1) % 26;
            ordinal = (char) (mod + 97) + ordinal;
            number = (number - mod) / 26;
        }

        return ordinal;
    }


    /**
     * Writes a raw object out to a JSON file with the specified name.
     *
     * @param name   Desired filename, sans extension
     * @param output Object to write. Most useful if you pass in either the Builder.analyze() response or the
     *               violations array it contains.
     */
    public static void writeResults(final String name, final Object output) {
        Writer writer = null;

        try {
            writer = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(name + ".json"), "utf-8"));

            writer.write(output.toString());
        } catch (IOException ignored) {
            logger.error("Exception in writeResults");
        } finally {
            try {
                if (writer != null) writer.close();
            } catch (NullPointerException | IOException e) {
                logger.error("Exception in writeResults");
            }
        }
    }

    /**
     * AxeRuntimeException represents an error returned from `axe.run()`.
     */

    public static class AxeRuntimeException extends RuntimeException {
        private static final long serialVersionUID = -123456789087654L;

        public AxeRuntimeException(String message) {
            super(message);
        }
    }

    /**
     * Chainable builder for invoking aXe. Instantiate a new Builder and configure testing with the include(),
     * exclude(), and options() methods before calling analyze() to run.
     */
    public static class Builder {
        private final WebDriver driver;
        private final URL script;
        private final List<String> includes = new ArrayList<String>();
        private final List<String> excludes = new ArrayList<String>();
        private String options = "{}";
        private Boolean skipFrames = false;
        private int timeout = 30;

        /**
         * Initializes the Builder class to chain configuration before analyzing pages.
         *
         * @param driver An initialized WebDriver
         * @param script The javascript URL of aXe
         */
        public Builder(final WebDriver driver, final URL script) {
            this.driver = driver;
            this.script = script;
        }


        /**
         * Include a selector.
         *
         * @param selector Any valid CSS selector
         */
        public Builder include(final String selector) {
            this.includes.add(selector);

            return this;
        }


        /**
         * Run aXe against the page.
         *
         * @return An aXe results document
         */
        public JSONObject analyze() throws JSONException {
            String axeContext;
            String axeOptions;

            if (includes.size() > 1 || excludes.size() > 0) {
                String includesJoined = "['" + StringUtils.join(includes, "'],['") + "']";
                String excludesJoined = excludes.size() == 0 ? "" : "['" + StringUtils.join(excludes, "'],['") + "']";

                axeContext = "document";
                axeOptions = String.format("{ include: [%s], exclude: [%s] }", includesJoined, excludesJoined);
            } else if (includes.size() == 1) {
                axeContext = String.format("'%s'", this.includes.get(0).replace("'", ""));
                axeOptions = options;
            } else {
                axeContext = "document";
                axeOptions = options;
            }

            String snippet = getAxeSnippet(axeContext, axeOptions);
            return execute(snippet);
        }

        private String getAxeSnippet(String context, String options) {
            return String.format(
                    "var callback = arguments[arguments.length - 1];" +
                            "var context = %s;" +
                            "var options = %s;" +
                            "var result = { error: '', results: null };" +
                            "axe.run(context, options, function (err, res) {" +
                            "  if (err) {" +
                            "    result.error = err.message;" +
                            "  } else {" +
                            "    result.results = res;" +
                            "  }" +
                            "  callback(result);" +
                            "});",
                    context, options
            );
        }


        private JSONObject execute(final String command, final Object... args) throws JSONException {
            AXE.inject(this.driver, this.script, this.skipFrames);

            this.driver.manage().timeouts().setScriptTimeout(this.timeout, TimeUnit.SECONDS);

            Object response = ((JavascriptExecutor) this.driver).executeAsyncScript(command, args);
            JSONObject result = new JSONObject((Map) response);
            String error = result.getString("error");

            // If the error is non-nil, raise a runtime error.
            if (error != null && !error.isEmpty()) {
                throw new AxeRuntimeException(error);
            }

            // If there was no error, there must have been results.
            JSONObject results = result.getJSONObject("results");
            return results;
        }
    }

}
