<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;

use Illuminate\Support\Facades\Input;
use Validator;
use Redirect;
use Session;

use Symfony\Component\Process\Process;
use Symfony\Component\Process\Exception\ProcessFailedException;

class ImageProcessorController extends Controller
{   
    
    public function getidentifier() {
        $identifier = ''.rand(11111,99999);
        $counter = 1;
        while (is_dir('output/'.$identifier)) {
            $identifier = ''.rand(11111,99999);
            $counter += 1;
            if($counter>5)
                return [
                    'success' => 0,
                ];
        }
        
        mkdir('uploads/'.$identifier);
        mkdir('output/'.$identifier);
        
        return [
            'success' => 1,
            'identifier' => $identifier,
        ];
    }
    
    public function upload() {
        $data = Input::all();
        $base64_image = $data['image'];
        $identifier =$data['identifier'];
        
        $image = base64_decode($base64_image);
        
        $destinationPath = 'uploads/'.$identifier; // upload path
        $extension = 'png';
        $fileName = rand(11111,99999).'.'.$extension; // image name 
        $path = $destinationPath.'/'.$fileName;
        file_put_contents($path, $image);
        return [
            'success' => 1,
            'path' => $fileName,
        ];
    }
    
    public function gcmkey() {
        $data = Input::all();
        $gcmkey = $data['token'];
        $identifier = $data['identifier'];
        
        $destinationPath = 'uploads'; // upload path
        $fileName = $identifier.'.txt'; // gcm identifier file name
        $path = $destinationPath.'/'.$fileName;
        
        file_put_contents($path, $gcmkey);
        
        //exec('start run.bat '.$identifier);
        
        //$schedule->command('start run.bat '.$identifier)->sendOutputTo('output/temp.txt');
        // $WshShell = new COM("WScript.Shell");
        // $oExec = $WshShell->Run('start run.bat '.$identifier, 0, false);
        
        
        pclose(popen('start run.bat '.$identifier, "r"));
        
        //$process = new Process('java -jar SegmentationJar.jar uploads/'.$identifier.' output/'.$identifier.' second.model'); 
        //$process = new Process('start run.bat '.$identifier);
        //java -jar SegmentationJar.jar uploads/'.$identifier.' output/'.$identifier.' classifier.model
        //$process->run();
        // while($process->isRunning()) {
            
        // }
        return "Process initiated";
    }
    
    public function purge() {
        $data = Input::all();
        $identifier = $data['identifier'];
        
        rmdir('output/'.$identifier);
    }
}
