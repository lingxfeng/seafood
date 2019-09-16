package com.eastinno.otransos.web.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * @author lengyu
 */
public abstract class NestedCheckedException extends Exception {
    private static final long serialVersionUID = 877787657678207546L;

    /** Root cause of this nested exception */
    private Throwable cause;

    /**
     * Construct a <code>NestedCheckedException</code> with the specified detail message.
     * 
     * @param msg the detail message
     */
    public NestedCheckedException(String msg) {
        super(msg);
    }

    /**
     * Construct a <code>NestedCheckedException</code> with the specified detail message and nested exception.
     * 
     * @param msg the detail message
     * @param cause the nested exception
     */
    public NestedCheckedException(String msg, Throwable cause) {
        super(msg);
        this.cause = cause;
    }

    /**
     * Return the nested cause, or <code>null</code> if none.
     * <p>
     * Note that this will only check one level of nesting. Use <code>getRootCause()</code> to retrieve the innermost
     * cause.
     * 
     * @see #getRootCause()
     */
    public Throwable getCause() {
        // Even if you cannot set the cause of this exception other than through
        // the constructor, we check for the cause being "this" here, as the cause
        // could still be set to "this" via reflection: for example, by a remoting
        // deserializer like Hessian's.
        return (this.cause == this ? null : this.cause);
    }

    /**
     * Return the detail message, including the message from the nested exception if there is one.
     */
    public String getMessage() {
        return NestedExceptionUtils.buildMessage(super.getMessage(), getCause());
    }

    /**
     * Print the composite message and the embedded stack trace to the specified stream.
     * 
     * @param ps the print stream
     */
    public void printStackTrace(PrintStream ps) {
        if (getCause() == null) {
            super.printStackTrace(ps);
        } else {
            ps.println(this);
            ps.print("Caused by: ");
            getCause().printStackTrace(ps);
        }
    }

    /**
     * Print the composite message and the embedded stack trace to the specified print writer.
     * 
     * @param pw the print writer
     */
    public void printStackTrace(PrintWriter pw) {
        if (getCause() == null) {
            super.printStackTrace(pw);
        } else {
            pw.println(this);
            pw.print("Caused by: ");
            getCause().printStackTrace(pw);
        }
    }

    /**
     * Retrieve the innermost cause of this exception, if any.
     * <p>
     * Currently just traverses NestedCheckedException causes. Will use the JDK 1.4 exception cause mechanism once
     * Spring requires JDK 1.4.
     * 
     * @return the innermost exception, or <code>null</code> if none
     */
    public Throwable getRootCause() {
        Throwable cause = getCause();
        if (cause instanceof NestedCheckedException) {
            return ((NestedCheckedException) cause).getRootCause();
        } else {
            return cause;
        }
    }

    /**
     * Check whether this exception contains an exception of the given class: either it is of the given class itself or
     * it contains a nested cause of the given class.
     * <p>
     * Currently just traverses NestedCheckedException causes. Will use the JDK 1.4 exception cause mechanism once
     * Spring requires JDK 1.4.
     * 
     * @param exClass the exception class to look for
     */
    public boolean contains(Class exClass) {
        if (exClass == null) {
            return false;
        }
        if (exClass.isInstance(this)) {
            return true;
        }
        Throwable cause = getCause();
        if (cause instanceof NestedCheckedException) {
            return ((NestedCheckedException) cause).contains(exClass);
        } else {
            return (cause != null && exClass.isInstance(cause));
        }
    }

}
