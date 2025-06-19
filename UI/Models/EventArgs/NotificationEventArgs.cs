using pos.Util;

namespace pos.Models.EventArgs;

public class NotificationEventArgs : System.EventArgs
{
    public required string Message { get; set; }
    public NotifConstants.NotifType NotifType { get; set; }
    public string[] Classes { get; set; } = ["Light"];
}