using System;

namespace pos.Extensions;

public static class ServiceLocator
{
    public static IServiceProvider Services { get; set; } = null!;
}
