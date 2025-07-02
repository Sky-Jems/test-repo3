using Pos.Util;

namespace pos.Models.EventArgs;

public class NotificationEventArgs : System.EventArgs
{
    public required string Message { get; set; }
    public Constants.NotifType NotifType { get; set; }
    public string[] Classes { get; set; } = ["Light"];
}