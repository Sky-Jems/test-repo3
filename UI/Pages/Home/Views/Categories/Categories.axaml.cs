using System;
using Avalonia.Controls;
using Avalonia.ReactiveUI;
using Pos.Controls;

namespace Pos.Pages.Home.Views.Categories;

public partial class Categories : ReactiveUserControl<CategoriesViewModel>
{
    public Categories()
    {
        InitializeComponent();
    }
}