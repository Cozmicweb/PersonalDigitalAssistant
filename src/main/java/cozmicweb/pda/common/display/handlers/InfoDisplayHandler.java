package cozmicweb.pda.common.display.handlers;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Base class for anything that can display text in the PDA information overlay.
 * <ul>
 *     <li>
 *         <b>Client-only handlers</b>:
 *         These can calculate their display text entirely on the client.
 *     </li>
 *     <li>
 *         <b>Server-synced handlers</b>:
 *         These need information that only the server knows.
 *     </li>
 * </ul>
 */
public abstract class InfoDisplayHandler {

    /**
     * <p>This map lives on the client-side handler instance and is updated when the
     * server responds to a data request. The {@link #getDisplayText()} method can
     * then read from this map when building the text shown on screen.</p>
     */
    protected final Map<String, Object> serverData = new HashMap<>();

    /**
     * Whether this handler needs to request data from the server.
     * <p>Return {@code false} if the display text can be generated entirely on the client.</p>
     * <p>Return {@code true} if the handler needs server-only data.</p>
     */
    public boolean requiresServerSync() {
        return false;
    }

    /**
     * The interval (in ticks) at which this handler should request data from the server.
     * <p>Only works if {@link #requiresServerSync()} is {@code true}.</p>
     */
    public int getUpdateInterval() {
        return 20;
    }

    /**
     * Higher numbers mean this handler will be displayed at the bottom of the list.
     */
    public int getPriority() {
        return 0;
    }

    /**
     * Builds the text displayed on the PDA overlay.
     * <p>This is called on the client during overlay rendering.</p>
     * <p>For client-only handlers, this can calculate the text directly.</p>
     * <p>For server-synced handlers, this should usually read from {@link #serverData},
     * because the real data is received asynchronously from the server.</p>
     */
    public Component getDisplayText() {
        return Component.empty();
    }

    /**
     * Provides extra parameters to send from the client to the server.
     *
     * <p>This is only used when {@link #requiresServerSync()} returns {@code true}.</p>
     *
     * <p>The values returned here are packed into the client-to-server request packet.
     * The server receives them as the {@code params} argument in
     * {@link #handleServerRequest(Player, Object[])}.</p>
     */
    public Object[] getServerDataParameters() {
        return new Object[0];
    }

    /**
     * Updates the client-side cached server data.
     *
     * <p>This is called on the client after the server responds to a request made by
     * this handler.</p>
     *
     * <p>The {@code data} array here is whatever the server returned from
     * {@link #handleServerRequest(Player, Object[])}.</p>
     *
     * <p>Used to validate the returned values and store them in
     * {@link #serverData}.</p>
     */
    public void updateServerData(Object[] data) {

    }

    /**
     * Handles a client data request on the server.
     *
     * <p>This is only used when {@link #requiresServerSync()} returns {@code true}.</p>
     *
     * <p>The server receives:</p>
     *
     * <ul>
     *     <li>the player who requested the data</li>
     *     <li>the parameters from {@link #getServerDataParameters()}</li>
     * </ul>
     *
     * <p>The returned array is sent back to the client. Once received, the client
     * passes that returned data into {@link #updateServerData(Object[])}.</p>
     *
     * <p>This method can safely read server-only data.</p>
     */
    public Object[] handleServerRequest(Player player, Object[] params) {
        return new Object[0];
    }

    /**
     * <p>Instead of writing:</p>
     *
     * <pre>
     * return new Object[]{"minecraft:zombie", 5};
     * </pre>
     *
     * <p>You can write:</p>
     *
     * <pre>
     * return vararg("minecraft:zombie", 5);
     * </pre>
     */
    protected static Object @NonNull [] vararg(Object... objects) {
        return objects;
    }

}