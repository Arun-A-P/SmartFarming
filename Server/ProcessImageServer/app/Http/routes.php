<?php

/*
|--------------------------------------------------------------------------
| Application Routes
|--------------------------------------------------------------------------
|
| Here is where you can register all of the routes for an application.
| It's a breeze. Simply tell Laravel the URIs it should respond to
| and give it the controller to call when that URI is requested.
|
*/

Route::get('/', function () {
    return view('welcome');
});

Route::get('upload', function () {
    return view('uploadform');
});

Route::get('test', 'ImageProcessorController@scriptCallback');

Route::get('getidentifier', 'ImageProcessorController@getidentifier');
Route::post('upload', 'ImageProcessorController@upload');
Route::post('gcmkey', 'ImageProcessorController@gcmkey');
Route::post('purge', 'ImageProcessorController@purge');