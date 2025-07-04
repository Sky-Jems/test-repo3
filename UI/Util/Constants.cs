
namespace Pos.Util;

public static class Constants
{
    public enum NotifType
    {
        Information,
        Success,
        Warning,
        Error
    }

    public enum OrderStatusType
    {
        PENDING,
        COMPLETED,
        CANCELED
    }

    public enum OrderDiscountType
    {
        LineItem,
        Order
    }

    public static class Role
    {
        public static string Staff = "Staff";
        public static string Admin = "Admin";
    }
}