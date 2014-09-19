package jenkins;

import hudson.FilePath;
import hudson.remoting.Channel;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.io.File;

/**
 * Inspects {@link FilePath} access from remote channels.
 *
 * @author Kohsuke Kawaguchi
 * @see FilePath
 * @since 1.THU
 */
public abstract class FilePathFilter {
    /**
     * Checks if the given file/directory can be read.
     *
     * On POSIX, this corresponds to the 'r' permission of the file/directory itself.
     */
    public void read(File f) throws SecurityException {}

    /**
     * Checks if the given file can be written.
     *
     * On POSIX, this corresponds to the 'w' permission of the file itself.
     */
    public void write(File f) throws SecurityException {}

    /**
     * Checks if the given directory can be created.
     *
     * On POSIX, this corresponds to the 'w' permission of the parent directory.
     */
    public void mkdirs(File f) throws SecurityException {}

    /**
     * Checks if the given file can be created.
     *
     * On POSIX, this corresponds to the 'w' permission of the parent directory.
     */
    public void create(File f) throws SecurityException {}

    /**
     * Checks if the given file/directory can be deleted.
     *
     * On POSIX, this corresponds to the 'w' permission of the parent directory.
     */
    public void delete(File f) throws SecurityException {}

    /**
     * Checks if the metadata of the given file/directory (as opposed to the content) can be accessed.
     *
     * On POSIX, this corresponds to the 'r' permission of the parent directory.
     */
    public void stat(File f) throws SecurityException {}


    public void installTo(Channel ch) {
        synchronized (ch) {
            FilePathFilterAggregator filters = ch.getProperty(FilePathFilterAggregator.KEY);
            if (filters==null) {
                filters = new FilePathFilterAggregator();
                ch.setProperty(FilePathFilterAggregator.KEY,filters);
            }
            filters.add(this);
        }
    }

    public void uninstallFrom(Channel ch) {
        synchronized (ch) {
            FilePathFilterAggregator filters = ch.getProperty(FilePathFilterAggregator.KEY);
            if (filters!=null) {
                filters.remove(this);
            }
        }
    }

    /**
     * Returns an {@link FilePathFilter} object that represents all the in-scope filters,
     * or null if none is needed.
     */
    public static @CheckForNull FilePathFilter current() {
        Channel ch = Channel.current();
        if (ch==null)   return null;

        return ch.getProperty(FilePathFilterAggregator.KEY);
    }

    /**
     * A flavor of {@link #current()} that returns a place holder empty object if none is needed.
     *
     * @return
     *      never null. If no filter is in scope, this method returns a non-null object that
     *      does nothing.
     */
    public static @Nonnull FilePathFilter currentNonnull() {
        FilePathFilter f = current();
        if (f==null)   return NOOP;
        return f;
    }

    private static final FilePathFilter NOOP = new FilePathFilter() {
    };
}
