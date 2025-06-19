using pos.Models.EventArgs;

namespace pos.Util;

public static class NotificationUtil
{
    public static NotificationEventArgs Success(string message = "")
    {
        return new NotificationEventArgs
        {
            Message = message,
            NotifType = NotifConstants.NotifType.Success
        };
    }

    public static NotificationEventArgs Error(string message = "")
    {
        return new NotificationEventArgs
        {
            Message = message,
            NotifType = NotifConstants.NotifType.Error
        };
    }

    public static NotificationEventArgs Warning(string message = "")
    {
        return new NotificationEventArgs
        {
            Message = message,
            NotifType = NotifConstants.NotifType.Warning
        };
    }

    public static NotificationEventArgs Information(string message = "")
    {
        return new NotificationEventArgs
        {
            Message = message,
            NotifType = NotifConstants.NotifType.Information
        };
    }
}