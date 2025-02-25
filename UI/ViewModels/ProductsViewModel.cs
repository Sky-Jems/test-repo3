using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Runtime.CompilerServices;
using src.TmpModels;

namespace src.ViewModels
{
    public class ProductsViewModel : INotifyPropertyChanged
    {

        public ObservableCollection<Product> products { get; set; }

        public ProductsViewModel()
        {
            products = new ObservableCollection<Product>
            {
                new Product("Baked Shrimp", "$189.00"),
                new Product("Baked Shrimp", "$189.00"),
                new Product("Baked Shrimp", "$189.00"),
                new Product("Baked Shrimp", "$189.00"),
                new Product("Baked Shrimp", "$189.00"),
                new Product("Baked Shrimp", "$189.00"),
                new Product("Baked Shrimp", "$189.00"),
                new Product("Baked Shrimp", "$189.00"),
                new Product("Baked Shrimp", "$189.00"),
                new Product("Baked Shrimp", "$189.00"),
                new Product("Baked Shrimp", "$189.00"),
            };
        }

        public event PropertyChangedEventHandler PropertyChanged;

        protected void OnPropertyChanged([CallerMemberName] string propertyName = null)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
        }
    }
}