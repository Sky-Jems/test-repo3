using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Runtime.CompilerServices;
using System.Windows.Input;
using src.TmpModels;

// tentative view model for categories
// will use proper model for category when design is finalized
// for now, this will be used to populate the buttons and will be used to handle the button tap event
namespace src.ViewModels
{
    public class CategoriesViewModel : INotifyPropertyChanged
    {

        public ObservableCollection<Category> categories { get; set; }
        public IEnumerable<Category> RegularCategories => categories.Where(b => b.group == "Regular");
        public IEnumerable<Category> SpecialCategories => categories.Where(b => b.group == "Special");
        public IEnumerable<Category> SeasonalCategories => categories.Where(b => b.group == "Seasonal");


        public CategoriesViewModel()
        {
            categories = new ObservableCollection<Category>
            {
                new Category("SM PL", "Regular", Color.FromArgb("#BFDBFE"), "sml_plate.png"),
                new Category("BRK FST", "Regular", Color.FromArgb("#FDBA74"), "breakfast.png"),
                new Category("DSRT", "Regular", Color.FromArgb("#22C55E"), "dessert.png"),
                new Category("FL FVS", "Regular", Color.FromArgb("#CA8A04"), "fl_fvs.png"),
                new Category("MN DSH", "Regular", Color.FromArgb("#F472B6"), "main_dish.png"),
                new Category("PT NDL", "Regular", Color.FromArgb("#A78BFA"), "pasta.png"),
                new Category("PZ", "Regular", Color.FromArgb("#BBF7D0"), "pizza.png"),
                new Category("RCE", "Regular", Color.FromArgb("#FFFFFF"), "rice.png"),
                new Category("SLD", "Regular", Color.FromArgb("#F97316"), "salad.png"),
                new Category("SD BG", "Regular", Color.FromArgb("#A21CAF"), "burger.png"),
                new Category("SZ SPL", "Regular", Color.FromArgb("#0EA5E9"), "sizzling.png"),
                new Category("SPS", "Regular", Color.FromArgb("#FEF9C3"), "soup.png"),
                new Category("DKS", "Regular", Color.FromArgb("#FCE7F3"), "drinks.png"),
                new Category("BST SLR", "Special", Color.FromArgb("#FCD34D"), "soup.png"),
                new Category("NW", "Special", Color.FromArgb("#64748B"), "soup.png"),
                new Category("SK SPL", "Special", Color.FromArgb("#3F6212"), "soup.png"),
                new Category("VL SPL", "Seasonal", Color.FromArgb("#EF4444"), "soup.png"),
                new Category("HW SPL", "Seasonal", Color.FromArgb("#854D0E"), "soup.png"),
                new Category("XMAS", "Seasonal", Color.FromArgb("#F9A8D4"), "soup.png"),
            };
        }

        public event PropertyChangedEventHandler PropertyChanged;

        protected void OnPropertyChanged([CallerMemberName] string propertyName = null)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
        }
    }
}