'use strict';

/**
 * @ngdoc overview
 * @name minovateApp
 * @description
 * # minovateApp
 *
 * Main module of the application.
 */

/*jshint -W079 */

var app = angular
  .module('minovateApp', [
    'ngAnimate',
    'ngCookies',
    'ngResource',
    'ngSanitize',
    //  'ngTouch',
    //  'ngMessages',
    'picardy.fontawesome', //*** lost dashboard icon
    'ui.bootstrap',
    'ui.router',
    'ui.utils',
    'angular-loading-bar',
    //  'angular-momentjs',
    //  'FBAngular',
    //  'lazyModel',
    //  'toastr',
    'angularBootstrapNavTree',
    'oc.lazyLoad', //*** lost dashboard view
    'ui.select',
    'ui.tree',
    //  'textAngular',
    //  'colorpicker.module',
    'angularFileUpload',
    //   'ngImgCrop',
    'datatables',
    'datatables.bootstrap',
    'datatables.colreorder',
    'datatables.colvis',
    'datatables.tabletools',
    'datatables.scroller',
    'datatables.columnfilter',
    //'ui.grid',
    //'ui.grid.resizeColumns',
    //'ui.grid.edit',
    //'ui.grid.moveColumns',
    'ngTable',
    'smart-table',
    'angular-flot',
    //'angular-rickshaw',
    //'easypiechart',
    //'uiGmapgoogle-maps',
    'ui.calendar',
    //'ngTagsInput',
    'pascalprecht.translate', //*** lost dashboard
    'ngMaterial',
    //'localytics.directives',
    //'leaflet-directive',
    //'wu.masonry',
    //'ipsum',
    //'angular-intro',
    //'dragularModule'
  ])
  .run(['$rootScope', '$state', '$stateParams', function($rootScope, $state, $stateParams) {
    $rootScope.$state = $state;
    $rootScope.$stateParams = $stateParams;
    $rootScope.$on('$stateChangeSuccess', function(event, toState) {

      event.targetScope.$watch('$viewContentLoaded', function () {

        angular.element('html, body, #content').animate({ scrollTop: 0 }, 200);

        setTimeout(function () {
          angular.element('#wrap').css('visibility','visible');

          if (!angular.element('.dropdown').hasClass('open')) {
            angular.element('.dropdown').find('>ul').slideUp();
          }
        }, 200);
      });
      $rootScope.containerClass = toState.containerClass;
    });
  }])

  .config(['uiSelectConfig', function (uiSelectConfig) {
    uiSelectConfig.theme = 'bootstrap';
  }])

  //angular-language
  .config(['$translateProvider', function($translateProvider) {
    $translateProvider.useStaticFilesLoader({
      prefix: 'languages/',
      suffix: '.json'
    });
    $translateProvider.useLocalStorage();
    $translateProvider.preferredLanguage('en');
    $translateProvider.useSanitizeValueStrategy(null);
  }])

  .config(['$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider) {

    $urlRouterProvider.otherwise('/app_nl/home');

    $stateProvider

      .state('app', {
        abstract: true,
        url: '/app',
        templateUrl: 'views/app.html'
      })
        .state('app_nl', {
            abstract: true,
            url: '/app_nl',
            templateUrl: 'views/app_nl.html'
        })
      //app core pages (errors, login,signup)
      .state('site', {
        abstract: true,
        url: '/site',
        template: '<div ui-view></div>'
      })
      //login
      .state('site.login', {
        url: '/login',
        controller: 'LoginCtrl',
        templateUrl: 'views/site/login.html'
      })
      //signup
      .state('site.signup', {
        url: '/signup',
        controller: 'SignupCtrl',
        templateUrl: 'views/site/signup.html'
      })
      //Confirm OTP
        .state('site.confirmOTP', {
            url: '/confirmOTP/:uid',
            controller: 'ConfirmOTPCtrl',
            templateUrl: 'views/site/confirmOTP.html'
        })
      //forgot password
      .state('site.forgotpass', {
        url: '/forgotpass',
        controller: 'ForgotPasswordCtrl',
        templateUrl: 'views/site/forgotpass.html'
      })
      //page 404
      .state('site.page404', {
        url: '/page404',
        templateUrl: 'views/site/page404.html'
      })
      //homepage
        .state('app_nl.home', {
            url: '/home',
            controller: 'DashboardCtrl',
            templateUrl: 'views/site/home.html',
            resolve: {
                plugins: ['$ocLazyLoad', function($ocLazyLoad) {
                    return $ocLazyLoad.load([
                        'scripts/vendor/datatables/datatables.bootstrap.min.css',
                        'scripts/vendor/datatables/datatables.bootstrap.min.css'
                    ]);
                }]
            }
        })


      //Appointment related pages
      .state('app.appointment', {
        url: '/appointment',
        template: '<div ui-view></div>'
      })
      //Book an appointment
        .state('app.appointment.bookAnAppointment', {
            url: '/bookAnAppointment',
            controller: 'DashboardCtrl',
            templateUrl: 'views/appointment/bookAnAppointment.html',
            resolve: {
                plugins: ['$ocLazyLoad', function($ocLazyLoad) {
                    return $ocLazyLoad.load([
                        'scripts/vendor/datatables/datatables.bootstrap.min.css',
                        'scripts/vendor/datatables/datatables.bootstrap.min.css'
                    ]);
                }]
            }
        })
        //Confirm Appointment
        .state('app.appointment.confirmAppointment', {
            url: '/confirm-appointment',
            controller: 'ConfirmAppointmentCtrl',
            templateUrl: 'views/appointment/confirmAppointment.html'
        })


      //Record related pages
        .state('record', {
            url: '/record',
            template: '<div ui-view></div>'
        })
        //Logged in user pages
        //Dashboard
        .state('app.dashboard', {
            url: '/dashboard',
            controller: 'DashboardCtrl',
            templateUrl: 'views/user/dashboard.html',
            resolve: {
                plugins: ['$ocLazyLoad', function($ocLazyLoad) {
                    return $ocLazyLoad.load([
                        'scripts/vendor/datatables/datatables.bootstrap.min.css',
                        'scripts/vendor/datatables/datatables.bootstrap.min.css'
                    ]);
                }]
            }
        })
        //My appointments
        .state('app.myAppointments', {
            url: '/myAppointments',
            controller: 'MyAppointmentsCtrl',
            templateUrl: 'views/user/myAppointments.html'
        })
      //My Records page
      .state('app.myRecords', {
        url: '/myRecords',
        controller: 'MyHealthRecordsCtrl',
        templateUrl: 'views/user/myRecords.html',
        resolve: {
          plugins: ['$ocLazyLoad', function($ocLazyLoad) {
            return $ocLazyLoad.load([
              'scripts/vendor/filestyle/bootstrap-filestyle.min.js'
            ]);
          }]
        }
      })


    /*    //mail
     .state('app.mail', {
     abstract: true,
     url: '/mail',
     controller: 'MailCtrl',
     templateUrl: 'views/tmpl/mail/mail.html'
     })
     //mail/inbox
     .state('app.mail.inbox', {
     url: '/inbox',
     controller: 'MailInboxCtrl',
     templateUrl: 'views/tmpl/mail/inbox.html'
     })
     //mail/compose
     .state('app.mail.compose', {
     url: '/compose',
     controller: 'MailComposeCtrl',
     templateUrl: 'views/tmpl/mail/compose.html'
     })
     //mail/single
     .state('app.mail.single', {
     url: '/single',
     controller: 'MailSingleCtrl',
     templateUrl: 'views/tmpl/mail/single.html'
     })
     //ui
     .state('app.ui', {
     url: '/ui',
     template: '<div ui-view></div>'
     })
     //ui/typography
     .state('app.ui.typography', {
     url: '/typography',
     controller: 'TypographyCtrl',
     templateUrl: 'views/tmpl/ui/typography.html',
     resolve: {
     plugins: ['$ocLazyLoad', function($ocLazyLoad) {
     return $ocLazyLoad.load([
     'scripts/vendor/google-code-prettify/prettify.css',
     'scripts/vendor/google-code-prettify/sons-of-obsidian.css',
     'scripts/vendor/google-code-prettify/prettify.js'
     ]);
     }]
     }
     })
     //ui/lists
     .state('app.ui.lists', {
     url: '/lists',
     controller: 'ListsCtrl',
     templateUrl: 'views/tmpl/ui/lists.html'
     })
     //ui/buttons&icons
     .state('app.ui.buttons-icons', {
     url: '/buttons-icons',
     controller: 'ButtonsIconsCtrl',
     templateUrl: 'views/tmpl/ui/buttons-icons.html'
     })
     //ui/navs&accordions
     .state('app.ui.navs', {
     url: '/navs',
     controller: 'NavsCtrl',
     templateUrl: 'views/tmpl/ui/navs.html'
     })
     //ui/modals
     .state('app.ui.modals', {
     url: '/modals',
     controller: 'ModalsCtrl',
     templateUrl: 'views/tmpl/ui/modals.html'
     })
     //ui/tiles
     .state('app.ui.tiles', {
     url: '/tiles',
     controller: 'TilesCtrl',
     templateUrl: 'views/tmpl/ui/tiles.html'
     })
     //ui/portlets
     .state('app.ui.portlets', {
     url: '/portlets',
     controller: 'PortletsCtrl',
     templateUrl: 'views/tmpl/ui/portlets.html'
     })
     //ui/grid
     .state('app.ui.grid', {
     url: '/grid',
     controller: 'GridCtrl',
     templateUrl: 'views/tmpl/ui/grid.html'
     })
     //ui/widgets
     .state('app.ui.widgets', {
     url: '/widgets',
     controller: 'WidgetsCtrl',
     templateUrl: 'views/tmpl/ui/widgets.html'
     })
     //ui/alerts & notifications
     .state('app.ui.alerts', {
     url: '/alerts',
     controller: 'AlertsCtrl',
     templateUrl: 'views/tmpl/ui/alerts.html'
     })
     //ui/general
     .state('app.ui.general', {
     url: '/general',
     controller: 'GeneralCtrl',
     templateUrl: 'views/tmpl/ui/general.html'
     })
     //ui/tree
     .state('app.ui.tree', {
     url: '/tree',
     controller: 'TreeCtrl',
     templateUrl: 'views/tmpl/ui/tree.html'
     })
     //ui/masonry
     .state('app.ui.masonry', {
     url: '/masonry',
     controller: 'UiMasonryCtrl',
     templateUrl: 'views/tmpl/ui/masonry.html'
     })
     //ui/dragula
     .state('app.ui.dragula', {
     url: '/dragula',
     controller: 'UiDragulaCtrl',
     templateUrl: 'views/tmpl/ui/dragula.html'
     })
     //material
     .state('app.material', {
     url: '/material',
     template: '<div ui-view></div>'
     })
     //material/autocomplete
     .state('app.material.autocomplete', {
     url: '/autocomplete',
     controller: 'mtAutocompleteCtrl',
     templateUrl: 'views/tmpl/material/autocomplete.html'
     })
     //material/bottom-sheet
     .state('app.material.bottom-sheet', {
     url: '/bottom-sheet',
     controller: 'mtBottomSheetCtrl',
     templateUrl: 'views/tmpl/material/bottom-sheet.html'
     })
     //material/buttons
     .state('app.material.buttons', {
     url: '/buttons',
     controller: 'mtButtonsCtrl',
     templateUrl: 'views/tmpl/material/buttons.html'
     })
     //material/cards
     .state('app.material.cards', {
     url: '/cards',
     controller: 'mtCardsCtrl',
     templateUrl: 'views/tmpl/material/cards.html'
     })
     //material/checkbox
     .state('app.material.checkbox', {
     url: '/checkbox',
     controller: 'mtCheckboxCtrl',
     templateUrl: 'views/tmpl/material/checkbox.html'
     })
     //material/chips
     .state('app.material.chips', {
     url: '/chips',
     controller: 'mtChipsCtrl',
     templateUrl: 'views/tmpl/material/chips.html'
     })
     //material/content
     .state('app.material.content', {
     url: '/content',
     controller: 'mtContentCtrl',
     templateUrl: 'views/tmpl/material/content.html'
     })
     //material/dialog
     .state('app.material.dialog', {
     url: '/dialog',
     controller: 'mtDialogCtrl',
     templateUrl: 'views/tmpl/material/dialog.html'
     })
     //material/divider
     .state('app.material.divider', {
     url: '/divider',
     controller: 'mtDividerCtrl',
     templateUrl: 'views/tmpl/material/divider.html'
     })
     //material/fab-speed-dial
     .state('app.material.fab-speed-dial', {
     url: '/fab-speed-dial',
     controller: 'mtFabSpeedDialCtrl',
     templateUrl: 'views/tmpl/material/fab-speed-dial.html'
     })
     //material/fab-toolbar
     .state('app.material.fab-toolbar', {
     url: '/fab-toolbar',
     controller: 'mtFabToolbarCtrl',
     templateUrl: 'views/tmpl/material/fab-toolbar.html'
     })
     //material/grid-list
     .state('app.material.grid-list', {
     url: '/grid-list',
     controller: 'mtGridListCtrl',
     templateUrl: 'views/tmpl/material/grid-list.html'
     })
     //material/inputs
     .state('app.material.inputs', {
     url: '/inputs',
     controller: 'mtInputsCtrl',
     templateUrl: 'views/tmpl/material/inputs.html'
     })
     //material/list
     .state('app.material.list', {
     url: '/list',
     controller: 'mtListCtrl',
     templateUrl: 'views/tmpl/material/list.html'
     })
     //material/menu
     .state('app.material.menu', {
     url: '/menu',
     controller: 'mtMenuCtrl',
     templateUrl: 'views/tmpl/material/menu.html'
     })
     //material/progress-circular
     .state('app.material.progress-circular', {
     url: '/progress-circular',
     controller: 'mtProgressCircularCtrl',
     templateUrl: 'views/tmpl/material/progress-circular.html'
     })
     //material/progress-linear
     .state('app.material.progress-linear', {
     url: '/progress-linear',
     controller: 'mtProgressLinearCtrl',
     templateUrl: 'views/tmpl/material/progress-linear.html'
     })
     //material/radio-button
     .state('app.material.radio-button', {
     url: '/radio-button',
     controller: 'mtRadioButtonCtrl',
     templateUrl: 'views/tmpl/material/radio-button.html'
     })
     //material/select
     .state('app.material.select', {
     url: '/select',
     controller: 'mtSelectCtrl',
     templateUrl: 'views/tmpl/material/select.html'
     })
     //material/sidenav
     .state('app.material.sidenav', {
     url: '/sidenav',
     controller: 'mtSidenavCtrl',
     templateUrl: 'views/tmpl/material/sidenav.html'
     })
     //material/slider
     .state('app.material.slider', {
     url: '/slider',
     controller: 'mtSliderCtrl',
     templateUrl: 'views/tmpl/material/slider.html'
     })
     //material/subheader
     .state('app.material.subheader', {
     url: '/subheader',
     controller: 'mtSubheaderCtrl',
     templateUrl: 'views/tmpl/material/subheader.html'
     })
     //material/swipe
     .state('app.material.swipe', {
     url: '/swipe',
     controller: 'mtSwipeCtrl',
     templateUrl: 'views/tmpl/material/swipe.html'
     })
     //material/switch
     .state('app.material.switch', {
     url: '/switch',
     controller: 'mtSwitchCtrl',
     templateUrl: 'views/tmpl/material/switch.html'
     })
     //material/tabs
     .state('app.material.tabs', {
     url: '/tabs',
     controller: 'mtTabsCtrl',
     templateUrl: 'views/tmpl/material/tabs.html'
     })
     //material/toast
     .state('app.material.toast', {
     url: '/toast',
     controller: 'mtToastCtrl',
     templateUrl: 'views/tmpl/material/toast.html'
     })
     //material/toolbar
     .state('app.material.toolbar', {
     url: '/toolbar',
     controller: 'mtToolbarCtrl',
     templateUrl: 'views/tmpl/material/toolbar.html'
     })
     //material/tooltip
     .state('app.material.tooltip', {
     url: '/tooltip',
     controller: 'mtTooltipCtrl',
     templateUrl: 'views/tmpl/material/tooltip.html'
     })
     //material/whiteframe
     .state('app.material.whiteframe', {
     url: '/whiteframe',
     controller: 'mtWhiteframeCtrl',
     templateUrl: 'views/tmpl/material/whiteframe.html'
     })
     //shop
     .state('app.shop', {
     url: '/shop',
     template: '<div ui-view></div>'
     })
     //shop/orders
     .state('app.shop.orders', {
     url: '/orders',
     controller: 'OrdersCtrl',
     templateUrl: 'views/tmpl/shop/orders.html',
     resolve: {
     plugins: ['$ocLazyLoad', function($ocLazyLoad) {
     return $ocLazyLoad.load([
     'scripts/vendor/datatables/datatables.bootstrap.min.css',
     'scripts/vendor/datatables/Pagination/input.js',
     'scripts/vendor/datatables/ColumnFilter/jquery.dataTables.columnFilter.js'
     ]);
     }]
     }
     })
     //shop/products
     .state('app.shop.products', {
     url: '/products',
     controller: 'ProductsCtrl',
     templateUrl: 'views/tmpl/shop/products.html',
     resolve: {
     plugins: ['$ocLazyLoad', function($ocLazyLoad) {
     return $ocLazyLoad.load([
     'scripts/vendor/datatables/datatables.bootstrap.min.css',
     'scripts/vendor/datatables/Pagination/input.js',
     'scripts/vendor/datatables/ColumnFilter/jquery.dataTables.columnFilter.js'
     ]);
     }]
     }
     })
     //shop/invoices
     .state('app.shop.invoices', {
     url: '/invoices',
     controller: 'InvoicesCtrl',
     templateUrl: 'views/tmpl/shop/invoices.html',
     resolve: {
     plugins: ['$ocLazyLoad', function($ocLazyLoad) {
     return $ocLazyLoad.load([
     'scripts/vendor/datatables/datatables.bootstrap.min.css',
     'scripts/vendor/datatables/Pagination/input.js',
     'scripts/vendor/datatables/ColumnFilter/jquery.dataTables.columnFilter.js'
     ]);
     }]
     }
     })
     //shop/single-order
     .state('app.shop.single-order', {
     url: '/single-order',
     controller: 'SingleOrderCtrl',
     templateUrl: 'views/tmpl/shop/single-order.html',
     resolve: {
     plugins: ['$ocLazyLoad', function($ocLazyLoad) {
     return $ocLazyLoad.load([
     'scripts/vendor/datatables/datatables.bootstrap.min.css',
     'scripts/vendor/datatables/Pagination/input.js',
     'scripts/vendor/datatables/ColumnFilter/jquery.dataTables.columnFilter.js'
     ]);
     }]
     }
     })
     //shop/single-product
     .state('app.shop.single-product', {
     url: '/single-product',
     controller: 'SingleProductCtrl',
     templateUrl: 'views/tmpl/shop/single-product.html',
     resolve: {
     plugins: ['$ocLazyLoad', function($ocLazyLoad) {
     return $ocLazyLoad.load([
     'scripts/vendor/datatables/datatables.bootstrap.min.css',
     'scripts/vendor/datatables/Pagination/input.js',
     'scripts/vendor/datatables/ColumnFilter/jquery.dataTables.columnFilter.js',
     'scripts/vendor/touchspin/jquery.bootstrap-touchspin.js',
     'scripts/vendor/touchspin/jquery.bootstrap-touchspin.css',
     'scripts/vendor/magnific/magnific-popup.css',
     'scripts/vendor/magnific/jquery.magnific-popup.min.js'
     ]);
     }]
     }
     })
     //shop/single-invoice
     .state('app.shop.single-invoice', {
     url: '/single-invoice',
     controller: 'SingleInvoiceCtrl',
     templateUrl: 'views/tmpl/shop/single-invoice.html',
     resolve: {
     plugins: ['$ocLazyLoad', function($ocLazyLoad) {
     return $ocLazyLoad.load([
     'scripts/vendor/datatables/datatables.bootstrap.min.css',
     'scripts/vendor/datatables/Pagination/input.js',
     'scripts/vendor/datatables/ColumnFilter/jquery.dataTables.columnFilter.js'
     ]);
     }]
     }
     })
     //forms
     .state('app.forms', {
     url: '/forms',
     template: '<div ui-view></div>'
     })
     //forms/common
     .state('app.forms.common', {
     url: '/common',
     controller: 'FormsCommonCtrl',
     templateUrl: 'views/tmpl/forms/common.html',
     resolve: {
     plugins: ['$ocLazyLoad', function($ocLazyLoad) {
     return $ocLazyLoad.load([
     'scripts/vendor/slider/bootstrap-slider.js',
     'scripts/vendor/touchspin/jquery.bootstrap-touchspin.js',
     'scripts/vendor/touchspin/jquery.bootstrap-touchspin.css',
     'scripts/vendor/filestyle/bootstrap-filestyle.min.js'
     ]);
     }]
     }
     })
     //forms/validate
     .state('app.forms.validate', {
     url: '/validate',
     controller: 'FormsValidateCtrl',
     templateUrl: 'views/tmpl/forms/validate.html'
     })
     //forms/wizard
     .state('app.forms.wizard', {
     url: '/wizard',
     controller: 'FormWizardCtrl',
     templateUrl: 'views/tmpl/forms/wizard.html'
     })
     //forms/upload
     .state('app.forms.upload', {
     url: '/upload',
     controller: 'FormUploadCtrl',
     templateUrl: 'views/tmpl/forms/upload.html',
     resolve: {
     plugins: ['$ocLazyLoad', function($ocLazyLoad) {
     return $ocLazyLoad.load([
     'scripts/vendor/filestyle/bootstrap-filestyle.min.js'
     ]);
     }]
     }
     })

     //tables
     .state('app.tables', {
     url: '/tables',
     template: '<div ui-view></div>'
     })
     //tables/bootstrap
     .state('app.tables.bootstrap', {
     url: '/bootstrap',
     controller: 'TablesBootstrapCtrl',
     templateUrl: 'views/tmpl/tables/bootstrap.html'
     })
     //tables/datatables
     .state('app.tables.datatables', {
     url: '/datatables',
     controller: 'TablesDatatablesCtrl',
     templateUrl: 'views/tmpl/tables/datatables.html',
     resolve: {
     plugins: ['$ocLazyLoad', function($ocLazyLoad) {
     return $ocLazyLoad.load([
     'scripts/vendor/datatables/ColReorder/css/dataTables.colReorder.min.css',
     'scripts/vendor/datatables/ColReorder/js/dataTables.colReorder.min.js',
     'scripts/vendor/datatables/Responsive/dataTables.responsive.css',
     'scripts/vendor/datatables/Responsive/dataTables.responsive.js',
     'scripts/vendor/datatables/ColVis/css/dataTables.colVis.min.css',
     'scripts/vendor/datatables/ColVis/js/dataTables.colVis.min.js',
     'scripts/vendor/datatables/TableTools/css/dataTables.tableTools.css',
     'scripts/vendor/datatables/TableTools/js/dataTables.tableTools.js',
     'scripts/vendor/datatables/datatables.bootstrap.min.css'
     ]);
     }]
     }
     })
     //tables/uiGrid
     .state('app.tables.ui-grid', {
     url: '/ui-grid',
     controller: 'TablesUiGridCtrl',
     templateUrl: 'views/tmpl/tables/ui-grid.html'
     })
     //tables/ngTable
     .state('app.tables.ng-table', {
     url: '/ng-table',
     controller: 'TablesNgTableCtrl',
     templateUrl: 'views/tmpl/tables/ng-table.html'
     })
     //tables/smartTable
     .state('app.tables.smart-table', {
     url: '/smart-table',
     controller: 'TablesSmartTableCtrl',
     templateUrl: 'views/tmpl/tables/smart-table.html'
     })
     //tables/fooTable
     .state('app.tables.footable', {
     url: '/footable',
     controller: 'TablesFootableCtrl',
     templateUrl: 'views/tmpl/tables/footable.html',
     resolve: {
     plugins: ['$ocLazyLoad', function($ocLazyLoad) {
     return $ocLazyLoad.load([
     'scripts/vendor/footable/dist/footable.all.min.js',
     'scripts/vendor/footable/css/footable.core.min.css'
     ]);
     }]
     }
     })
     */
    /*
     //layouts
     .state('app.layouts', {
     url: '/layouts',
     template: '<div ui-view></div>'
     })
     //layouts/boxed
     .state('app.layouts.boxed', {
     url: '/boxed',
     controller: 'BoxedlayoutCtrl',
     templateUrl: 'views/tmpl/layouts/boxed.html',
     containerClass: 'boxed-layout'
     })
     //layouts/fullwidth
     .state('app.layouts.fullwidth', {
     url: '/fullwidth',
     controller: 'FullwidthlayoutCtrl',
     templateUrl: 'views/tmpl/layouts/fullwidth.html'
     })
     //layouts/sidebar-sm
     .state('app.layouts.sidebar-sm', {
     url: '/sidebar-sm',
     controller: 'SidebarsmlayoutCtrl',
     templateUrl: 'views/tmpl/layouts/sidebar-sm.html',
     containerClass: 'sidebar-sm-forced sidebar-sm'
     })
     //layouts/sidebar-xs
     .state('app.layouts.sidebar-xs', {
     url: '/sidebar-xs',
     controller: 'SidebarxslayoutCtrl',
     templateUrl: 'views/tmpl/layouts/sidebar-xs.html',
     containerClass: 'sidebar-xs-forced sidebar-xs'
     })



     //profile page
     .state('app.pages.profile', {
     url: '/profile',
     controller: 'ProfileCtrl',
     templateUrl: 'views/tmpl/pages/profile.html',
     resolve: {
     plugins: ['$ocLazyLoad', function($ocLazyLoad) {
     return $ocLazyLoad.load([
     'scripts/vendor/filestyle/bootstrap-filestyle.min.js'
     ]);
     }]
     }
     })
     */
  }]);

