using System.Collections.ObjectModel;
using Avalonia.Media;
using Pos.Models;
using Pos.ViewModels;

namespace Pos.Pages.Home;

public partial class HomeViewModel : ViewModelBase
{
    public ObservableCollection<Category> Regulars { get; set; }
    public ObservableCollection<Category> Specials { get; set; }
    public ObservableCollection<Category> Seasonals { get; set; }
    
    public HomeViewModel() {
        Regulars =
        [
            new(1, "SM PL", "Regular", "#BFDBFE", "sml_plate.svg"),
            new(2, "BRK FST", "Regular", "#FDBA74", "breakfast.svg"),
            new(3, "DSRT", "Regular", "#22C55E", "dessert.svg"),
            new(4, "FL FVS", "Regular", "#CA8A04", "fl_fvs.svg"),
            new(5, "MN DSH", "Regular", "#F472B6", "main_dish.svg"),
            new(6, "PT NDL", "Regular", "#A78BFA", "pasta.svg"),
            new(7, "PZ", "Regular", "#BBF7D0", "pizza.svg"),
            new(8, "RCE", "Regular", "#FFFFFF", "rice.svg"),
            new(9, "SLD", "Regular", "#F97316", "salad.svg"),
            new(10, "SD BG", "Regular", "#A21CAF", "burger.svg", "#FFE9C1"),
            new(11, "SZ SPL", "Regular", "#0EA5E9", "sizzling.svg"),
            new(12, "SPS", "Regular", "#FEF9C3", "soup.svg"),
            new(13, "DKS", "Regular", "#FCE7F3", "drinks.svg"),
        ];
        Specials =
        [
            new(1, "BST SLR", "Special", "#FCD34D", "soup.svg"),
            new(2, "NW", "Special", "#64748B", "soup.svg", "#FFE9C1"),
            new(3, "SK SPL", "Special", "#3F6212", "soup.svg", "#FFE9C1"),
        ];
        Seasonals =
        [
            new(1, "VL SPL", "Seasonal", "#EF4444", "soup.svg", "#FFE9C1"),
            new(2, "HW SPL", "Seasonal", "#854D0E", "soup.svg", "#FFE9C1"),
            new(3, "XMAS", "Seasonal", "#F9A8D4", "soup.svg"),
        ];
    }
}