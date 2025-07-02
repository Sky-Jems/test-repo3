using System;
using Avalonia.Controls;
using Avalonia.Interactivity;
using Avalonia.ReactiveUI;
using ReactiveUI;

namespace Pos.Pages;

public partial class StaffLoginView : ReactiveUserControl<StaffLoginViewModel>
{
    public StaffLoginView()
    {
        InitializeComponent();
        this.DataContextChanged += (_, __) =>
        {
            if (DataContext is StaffLoginViewModel vm)
            {
                vm.LoadStaffCommand.Execute().Subscribe();
            }
        };
    }
}
