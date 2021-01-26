import {ECrudViewerInputType, ICrudViewerConfiguration} from './types/CrudViewerConfiguration';

export const crudRoutes: ICrudViewerConfiguration[] = [
    {
        path: 'departments',
        baseApi: 'departments',
        fields: [
            {title: 'Id', path: 'id', isId: true},
            {title: 'Title', path: 'title'},
        ]
    },
    {
        path: 'workers',
        baseApi: 'workers',
        fields: [
            {title: 'Id', path: 'id', isId: true},
            {title: 'First name', path: 'firstName'},
            {title: 'Last name', path: 'lastName'},
            {
                title: 'User', path: 'user.id', writePath: 'userId',
                type: ECrudViewerInputType.Select, params: 'users'
            },
            {
                title: 'Department', path: 'department.id', writePath: 'departmentId',
                type: ECrudViewerInputType.Select, params: 'departments'
            }
        ]
    },
    {
        path: 'users',
        baseApi: 'users',
        fields: [
            {title: 'Id', path: 'id', isId: true},
            {title: 'Username', path: 'username'},
            {title: 'Role', path: 'role'}
        ]
    },
    {
        path: 'calendar-days',
        baseApi: 'calendar-days',
        fields: [
            {title: 'Id', path: 'id', isId: true},
            {title: 'Date', path: 'date', type: ECrudViewerInputType.Date},
            {
                title: 'Status', path: 'status.title', writePath: 'statusId',
                type: ECrudViewerInputType.Select, params: 'calendar-day-status'
            }
        ],
        filterFields: [
            {title: 'Year', path: 'year', default: '2021'},
            {title: 'Month', path: 'month'}
        ]
    },
    {
        path: 'calendar-day-statuses',
        baseApi: 'calendar-day-statuses',
        fields: [
            {title: 'Id', path: 'id', isId: true},
            {title: 'Title', path: 'title'},
            {title: 'Short title', path: 'shortTitle'},
            {title: 'Hex color', path: 'hexColor', type: ECrudViewerInputType.Color},
            {title: 'Is default?', path: 'default', type: ECrudViewerInputType.Checkbox}
        ]
    },
    {
        path: 'workday-results',
        baseApi: 'workday-results',
        fields: [
            {title: 'Id', path: 'id', isId: true},
            {title: 'Date', path: 'date', type: ECrudViewerInputType.Date},
            {title: 'Status title', path: 'status.title'},
            {
                title: 'Status', writeOnly: true, path:'status.id', writePath: 'statusId',
                type: ECrudViewerInputType.Select, params: 'workday-result-status'
            },
            {
                title: 'Worker', path: 'worker.firstName', readOnly: true
            },
            {
                title: 'Worker', path: 'workerId', writeOnly: true,
                type: ECrudViewerInputType.Select, params: 'workers'
            }
        ]
    },
    {
        path: 'workday-result-statuses',
        baseApi: 'workday-result-statuses',
        fields: [
            {title: 'Id', path: 'id', isId: true},
            {title: 'Title', path: 'title'},
            {title: 'Short title', path: 'shortTitle'}
        ]
    }
];
