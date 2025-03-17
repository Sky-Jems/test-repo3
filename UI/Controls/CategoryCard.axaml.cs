using Avalonia;
using Avalonia.Controls;
using Pos.Models;

namespace Pos.Controls;

public partial class CategoryCard : UserControl
{
    public static readonly StyledProperty<Category> CategoryProperty = 
        AvaloniaProperty.Register<CategoryCard, Category>(nameof(Category));

    public Category Category
    {
        get => GetValue(CategoryProperty);
        set => SetValue(CategoryProperty, value);
    }

    public CategoryCard()
    {
        InitializeComponent();
    }
}