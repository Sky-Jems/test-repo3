using pos.Models.EventArgs;

namespace Pos.Util;

public static class NotificationUtil
{
    public static NotificationEventArgs Success(string message = "")
    {
        return new NotificationEventArgs
        {
            Message = message,
            NotifType = Constants.NotifType.Success
        };
    }

    public static NotificationEventArgs Error(string message = "")
    {
        return new NotificationEventArgs
        {
            Message = message,
            NotifType = Constants.NotifType.Error
        };
    }

    public static NotificationEventArgs Warning(string message = "")
    {
        return new NotificationEventArgs
        {
            Message = message,
            NotifType = Constants.NotifType.Warning
        };
    }

    public static NotificationEventArgs Information(string message = "")
    {
        return new NotificationEventArgs
        {
            Message = message,
            NotifType = Constants.NotifType.Information
        };
    }
}