import subprocess
import shutil
import sys
import argparse
from pathlib import Path

REF_OUTPUT = Path('.').resolve()/'ref_out'
NEW_REF_OUTPUT = Path('.').resolve()/'new_ref_out'
COLLABORATORS = Path('.').resolve()/'collaborators'

def check_collaborators():
    default_collabs = """Collaborators:

External resources:

I certify that other than listed above, I have not collaborated with anyone on this assignment nor have I used any external resources to assist me in completing this assignment: <put your name here>
"""

    collabs = None
    with COLLABORATORS.open('r') as f:
        collabs = f.read()

    if (collabs == default_collabs):
        print("Error: collaborators not edited!")
    else:
        print("Collaborators ok")
    sys.stdout.flush()

def test_compile():
    compile_output = subprocess.run(['make', 'clean'], stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    compile_output = subprocess.run(['make', 'compile'], stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    if compile_output.returncode != 0:
        print('Error: compilation failed!')
    else:
        print('Compilation ok')
    sys.stdout.flush()

def test_run_parti(i):
    subprocess.run(['make', 'compile'], stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    run_output = subprocess.run(['make', 'test' + str(i)], stdout=subprocess.PIPE, stderr=subprocess.PIPE, encoding='utf-8')
    if run_output.returncode != 0:
        print('Error: running program failed!')
    else:
        print('Running program ok')
    sys.stdout.flush()
    
    desired_output = None
    REF_OUTPUT = Path('.').resolve()/('part' + str(i) + '_out')
    with REF_OUTPUT.open('r') as f:
        desired_output = f.read()

    if run_output.stdout == desired_output and run_output.stderr == "":
        print("Program output ok")
    else:
        print("Error: incorrect program output!")
    sys.stdout.flush()

if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('--test', '-t', default=None, help="Test to run (1 = compile, 2-5 = run, 6 = run new tests)")
    
    args = parser.parse_args()

    if args.test == None:
        check_collaborators()
        test_compile()
        test_run_parti("1")
        test_run_parti("2")
        test_run_parti("3")
        test_run_parti("4")
    elif args.test == "1":
        check_collaborators()
        test_compile()
    elif args.test == "2":
        check_collaborators()
        test_run_parti("1")
    elif args.test == "3":
        check_collaborators()
        test_run_parti("2")
    elif args.test == "4":
        check_collaborators()
        test_run_parti("3")
    elif args.test == "5":
        check_collaborators()
        test_run_parti("4")
    elif args.test == "6":
        check_collaborators()
        test_run_parti("5")