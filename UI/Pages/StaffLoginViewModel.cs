using System;
using System.Collections.ObjectModel;
using System.Reactive;
using System.Threading.Tasks;
using pos.Api;
using Pos.Models;
using Pos.Pages;
using ReactiveUI;
using Pos.Util;

namespace Pos
{
    public class StaffLoginViewModel : ReactiveObject, IRoutableViewModel
    {
        private readonly IAuthService _authService;

        public string? UrlPathSegment => "staff-login";
        public IScreen HostScreen { get; }

        public ReactiveCommand<Staff, Unit> SelectStaffCommand { get; }
        public ReactiveCommand<Unit, Unit> LoadStaffCommand { get; }
        public ReactiveCommand<Unit, Unit> GoBack { get; }

        public ObservableCollection<Staff> StaffList { get; } = new ObservableCollection<Staff>();

        public StaffLoginViewModel(IScreen screen, IAuthService authService)
        {
            HostScreen = screen;
            _authService = authService ?? throw new ArgumentNullException(nameof(authService));

            SelectStaffCommand = ReactiveCommand.CreateFromTask<Staff>(LoginAndNavigateAsync);


            GoBack = ReactiveCommand.Create(() =>
            {
                HostScreen.Router.NavigationStack.Clear();
            });
            LoadStaffCommand = ReactiveCommand.CreateFromTask(LoadStaffAsync);
        }

        public async Task LoadStaffAsync()
        {
            var staffList = await _authService.GetStaffAsync();
            StaffList.Clear();

            foreach (var staff in staffList)
            {
                if (!string.IsNullOrWhiteSpace(staff.firstName))
                {
                    StaffList.Add(staff);
                }
            }
        }

        private async Task LoginAndNavigateAsync(Staff staff)
        {
            var token = await _authService.StaffLoginAsync(staff.Id);

            if (string.IsNullOrEmpty(token))
                throw new Exception("Login failed: Token is empty.");

            string FullName = staff.FullName;
            HostScreen.Router.Navigate.Execute(new HomePageViewModel(HostScreen, FullName, Constants.Role.Staff));
        }
    }
}
