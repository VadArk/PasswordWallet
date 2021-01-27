import {combineReducers} from 'redux';
import userReducer from './user/userReducer';
import authReducer from './user/auth/authReducer';
import passwordReducer from './password/passwordReducer';

const rootReducer = combineReducers({
    user: userReducer,
    password: passwordReducer,
    auth: authReducer
});

export default rootReducer;