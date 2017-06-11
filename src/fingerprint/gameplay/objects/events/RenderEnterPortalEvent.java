package fingerprint.gameplay.objects.events;

import fingerprint.gameplay.objects.Portal;

public class RenderEnterPortalEvent {
    private Portal portal;

    public RenderEnterPortalEvent(Portal portal) {
        this.portal = portal;
    }

    public Portal getPortal() {
        return portal;
    }
}
