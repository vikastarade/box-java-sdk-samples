package com.box.samples.aws.sns.validation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

/**
 * Validation context.
 */
public class Validation {

    /**
     * {@link JSONObject} constrain violation by path.
     */
    private final Map<String, List<JSONObject>> violationsByPath = new HashMap<>();

    /**
     * Constructor.
     */
    public Validation() {
    }

    /**
     * Adds constrain violation to the validation context.
     *
     * @param path
     *            to the violated value
     * @param message
     *            violation message
     */
    public void addError(String path, String message) {
        List<JSONObject> violations = violations(path);
        violations.add(new JSONObject().put("message", message));
    }

    /**
     * Adds constrain violation to the validation context.
     *
     * @param path
     *            to the violated value
     * @param message
     *            violation message
     * @param payload
     *            additional information for violation
     */
    public void addError(String path, String message, JSONObject payload) {
        List<JSONObject> violations = violations(path);
        violations.add(new JSONObject().put("message", message).put("payload", payload));
    }

    /**
     * Resolves container for violations for a provided path.
     *
     * @param path
     *            to the violated value
     * @return violations container
     */
    private List<JSONObject> violations(String path) {
        return this.violationsByPath.computeIfAbsent(path, p -> new LinkedList<>());
    }

    /**
     * @return True if there is no violation.
     */
    public boolean isValid() {
        return this.violationsByPath.isEmpty();
    }

    /**
     * Enforces valid state.
     *
     * @throws ValidationException
     *             if {@link #isValid()} is false.
     */
    public void validate() {
        if (!this.isValid()) {
            throw new ValidationException(this);
        }
    }

    /**
     * @return Builds {@link JSONObject} for current state.
     */
    public JSONObject toJSON() {
        if (this.isValid()) {
            return null;
        }
        JSONObject result = new JSONObject();
        this.violationsByPath.forEach(result::put);
        return result;
    }

}
